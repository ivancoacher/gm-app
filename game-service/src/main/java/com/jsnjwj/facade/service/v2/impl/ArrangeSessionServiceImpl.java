package com.jsnjwj.facade.service.v2.impl;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.entity.GameAreaEntity;
import com.jsnjwj.facade.manager.GameGroupingManager;
import com.jsnjwj.facade.query.GameGroupingAreaSetQuery;
import com.jsnjwj.facade.query.GameGroupingSetNumQuery;
import com.jsnjwj.facade.service.v2.ArrangeSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 场地安排
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ArrangeSessionServiceImpl implements ArrangeSessionService {

    private final GameGroupingManager gameGroupingManager;

    /**
     * 创建场地
     */
    @Override
    public ApiResponse<?> setCourtNum(GameGroupingSetNumQuery query) {
        if (query.getAreaNum() <= 0)
            return ApiResponse.error("请输入正确的场地数");
        int courtNum = 1;
        gameGroupingManager.resetCourt(query.getGameId());

        List<GameAreaEntity> areas = new ArrayList<>();
        while (courtNum <= query.getAreaNum()) {
            GameAreaEntity area = new GameAreaEntity();
            area.setGameId(query.getGameId());
            area.setAreaName("场地" + courtNum);
            area.setAreaNo(courtNum);
            area.setStatus(1);
            areas.add(area);
            courtNum++;
        }
        gameGroupingManager.saveCourts(areas);
        return ApiResponse.success(true);
    }

    /**
     * 保存场地
     *
     * @param query
     * @return
     */
    @Override
    public ApiResponse<Boolean> saveCourt(GameGroupingAreaSetQuery query) {
        GameAreaEntity area = new GameAreaEntity();
        area.setId(query.getAreaId());
        area.setGameId(query.getGameId());
        area.setAreaName(query.getAreaName());
        area.setStatus(query.getStatus());
        gameGroupingManager.saveCourt(area);
        return ApiResponse.success(true);
    }

    /**
     * 获取所有场地对应场次信息
     *
     * @param gameId
     * @return
     */
    @Override
    public ApiResponse<List<GameAreaEntity>> getCourts(Long gameId) {
        List<GameAreaEntity> response = gameGroupingManager.getCourts(gameId);
        return ApiResponse.success(response);
    }


}

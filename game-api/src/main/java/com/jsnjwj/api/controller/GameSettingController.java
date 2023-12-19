package com.jsnjwj.api.controller;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.common.utils.ThreadLocalUtil;
import com.jsnjwj.facade.entity.GameAreaEntity;
import com.jsnjwj.facade.query.GameGroupingAreaSetQuery;
import com.jsnjwj.facade.query.GameGroupingSetNumQuery;
import com.jsnjwj.facade.service.GameSettingService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 赛事设置
 */
@RestController
@RequestMapping("/game/setting")
public class GameSettingController {

    @Resource
    private GameSettingService gameSettingService;

    /**
     * 场地列表
     *
     * @return
     */
    @GetMapping("/getAreas")
    public ApiResponse<List<GameAreaEntity>> getAreas() {
        Long gameId = ThreadLocalUtil.getCurrentGameId();
        return gameSettingService.getCourts(gameId);
    }

    /**
     * 设置场地数量
     *
     * @param query
     * @return
     */
    @PostMapping("/setAreaNum")
    public ApiResponse<?> setAreaNum(@RequestBody GameGroupingSetNumQuery query) {
        query.setGameId(ThreadLocalUtil.getCurrentGameId());
        return gameSettingService.setCourtNum(query);
    }

    /**
     * 保存场地信息
     *
     * @param query
     * @return
     */
    @PostMapping("/saveArea")
    public ApiResponse<Boolean> saveArea(@RequestBody GameGroupingAreaSetQuery query) {
        query.setGameId(ThreadLocalUtil.getCurrentGameId());
        return gameSettingService.saveCourt(query);
    }

}

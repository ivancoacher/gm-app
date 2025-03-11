package com.jsnjwj.facade.service.v2.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.dto.SessionItemDto;
import com.jsnjwj.facade.entity.GameItemEntity;
import com.jsnjwj.facade.entity.GameSessionEntity;
import com.jsnjwj.facade.entity.GameSessionItemEntity;
import com.jsnjwj.facade.manager.ArrangeSessionItemManager;
import com.jsnjwj.facade.manager.GameItemManager;
import com.jsnjwj.facade.mapper.GameSessionMapper;
import com.jsnjwj.facade.query.GameItemListQuery;
import com.jsnjwj.facade.query.session.GameGroupingSessionSetNumQuery;
import com.jsnjwj.facade.query.session.GameGroupingSessionSetQuery;
import com.jsnjwj.facade.query.session.SessionItemGetQuery;
import com.jsnjwj.facade.query.session.SessionItemSetQuery;
import com.jsnjwj.facade.service.v2.ArrangeSessionItemService;
import com.jsnjwj.facade.vo.session.SessionItemVo;
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
public class ArrangeSessionItemServiceImpl implements ArrangeSessionItemService {

    private final ArrangeSessionItemManager arrangeSessionItemManager;

    private final GameItemManager gameItemManager;
    private final GameSessionMapper gameSessionMapper;

    /**
     * 查询剩余未排场次项目
     * @param query
     * @return
     */
    @Override
    public ApiResponse<?> getUnSelectedItem(SessionItemGetQuery query) {
        // 查询已排场次项目
        List<Long> selectedItemIds = arrangeSessionItemManager.selectAllSelectedItemIds(query.getGameId());
        // 排除已排
        LambdaQueryWrapper<GameItemEntity> query1 = new LambdaQueryWrapper<>();
        query1.eq(GameItemEntity::getGameId,query.getGameId());
        if (CollectionUtil.isNotEmpty(selectedItemIds)){
            query1.notIn(GameItemEntity::getId,selectedItemIds);
        }
        List<GameItemEntity> list = gameItemManager.fetchListByWrapper(query1);
        return ApiResponse.success(list);
    }

    /**
     * 查询当前场次已排项目
     * @param query
     * @return
     */
    @Override
    public ApiResponse<SessionItemDto> getSelectedItem(SessionItemGetQuery query) {
        SessionItemDto sessionItemDto = new SessionItemDto();
        sessionItemDto.setGameId(query.getGameId());
        List<SessionItemVo> list = arrangeSessionItemManager.fetchBySessionId(query.getGameId(),query.getSessionId());
        sessionItemDto.setData(list);
        GameSessionEntity session = gameSessionMapper.selectById(query.getSessionId());
        sessionItemDto.setSessionName(session.getSessionName());
        return ApiResponse.success(sessionItemDto);
    }

    /**
     * 保存场次-项目关系
     * @param query
     * @return
     */
    @Override
    public ApiResponse<Boolean> saveSessionItem(SessionItemSetQuery query) {

        arrangeSessionItemManager.deleteBySessionId(query.getGameId(),query.getSessionId());

        List<GameSessionItemEntity> result = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(query.getData())){
            query.getData().forEach(item->{
                GameSessionItemEntity gameSessionItemEntity = new GameSessionItemEntity();
                gameSessionItemEntity.setGameId(query.getGameId());
                gameSessionItemEntity.setSessionId(query.getSessionId());
                gameSessionItemEntity.setGameId(query.getGameId());
                gameSessionItemEntity.setGameId(query.getGameId());
                gameSessionItemEntity.setGameId(query.getGameId());

                result.add(gameSessionItemEntity);
            });

            arrangeSessionItemManager.saveBatch(result);
        }


        return ApiResponse.success(true);
    }
}

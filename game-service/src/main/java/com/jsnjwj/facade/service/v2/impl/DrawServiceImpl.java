package com.jsnjwj.facade.service.v2.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.dao.SessionDrawDao;
import com.jsnjwj.facade.dao.SessionDrawListDao;
import com.jsnjwj.facade.dto.ArrangeDrawDto;
import com.jsnjwj.facade.entity.GameDrawEntity;
import com.jsnjwj.facade.entity.GameSessionEntity;
import com.jsnjwj.facade.entity.GameSessionItemEntity;
import com.jsnjwj.facade.entity.SignSingleEntity;
import com.jsnjwj.facade.enums.DrawTypeEnum;
import com.jsnjwj.facade.manager.ArrangeDrawManager;
import com.jsnjwj.facade.manager.ArrangeSessionItemManager;
import com.jsnjwj.facade.manager.ArrangeSessionManager;
import com.jsnjwj.facade.manager.SignApplyManager;
import com.jsnjwj.facade.mapper.GameDrawMapper;
import com.jsnjwj.facade.query.session.ManualDrawQuery;
import com.jsnjwj.facade.query.session.SystemDrawQuery;
import com.jsnjwj.facade.service.v2.DrawService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 抽签分组
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DrawServiceImpl implements DrawService {

    private final ArrangeSessionManager sessionManager;

    private final ArrangeSessionItemManager sessionItemManager;

    private final SignApplyManager signApplyManager;

    private final ArrangeDrawManager drawManager;
    private final GameDrawMapper gameDrawMapper;

    /**
     * 系统自动抽签分组
     */
    @Override
    public ApiResponse<?> systemDraw(SystemDrawQuery query) {
        // 判断抽签分组类型
        Integer type = query.getType();
        Long gameId = query.getGameId();
        List<GameDrawEntity> result = new ArrayList<>();
        // 按场次抽签
        if (DrawTypeEnum.DRAW_WITH_SESSION.getType() == type) {
            // 查询所有场次
            List<GameSessionEntity> sessionList = sessionManager.getList(query.getGameId());
            if (CollectionUtil.isEmpty(sessionList)) {
                return ApiResponse.error("请先创建场次");
            }
            LambdaQueryWrapper<GameDrawEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(GameDrawEntity::getGameId, gameId);
            gameDrawMapper.delete(queryWrapper);
            // 查询各个场次内对应的小项
            sessionList.forEach(session -> {
                List<GameSessionItemEntity> sessionItemEntities = sessionItemManager.fetchListBySessionId(gameId, session.getId());
                if (CollectionUtil.isEmpty(sessionItemEntities)) {
                    // 该场次未安排小项，则不处理
                    log.info("该场次未安排小项，则不处理");
                    return;
                }
                List<Long> itemIds = sessionItemEntities.stream().map(GameSessionItemEntity::getItemId).collect(Collectors.toList());
                List<SignSingleEntity> signApplyManagers = signApplyManager.getApplyByItemIds(gameId, itemIds);
                if (CollectionUtil.isEmpty(signApplyManagers)) {
                    log.info("该项目无报名记录");
                    return;
                }
                List<Long> signIds = signApplyManagers.stream().map(SignSingleEntity::getId).collect(Collectors.toList());
                // 将各个场次内的小项打乱，排序。相同选手，间隔5位

                Collections.shuffle(signIds);

                for (int i = 0; i < signIds.size(); i++) {
                    GameDrawEntity gameDrawEntity = new GameDrawEntity();
                    gameDrawEntity.setGameId(gameId);
                    gameDrawEntity.setSessionId(session.getId());
                    gameDrawEntity.setSessionNo(session.getSessionNo());
                    gameDrawEntity.setSignId(signIds.get(i));
                    gameDrawEntity.setSort(i);
                    gameDrawEntity.setCreatedAt(new Date());
                    result.add(gameDrawEntity);
                }
            });
        }
        // 不按场次抽签
        else {
            LambdaQueryWrapper<GameDrawEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(GameDrawEntity::getGameId, gameId);
            gameDrawMapper.delete(queryWrapper);
            // 创建默认场次
            GameSessionEntity gameSessionEntity = new GameSessionEntity();
            gameSessionEntity.setSessionNo(0);
            // 将所有小项安排进该场次中
            List<SignSingleEntity> signApplyManagers = signApplyManager.getSingleByGameId(gameId);
            if (CollectionUtil.isNotEmpty(signApplyManagers)) {
                List<Long> signIds = signApplyManagers.stream().map(SignSingleEntity::getId).collect(Collectors.toList());
                Collections.shuffle(signIds);
                for (int i = 0; i < signIds.size(); i++) {
                    GameDrawEntity gameDrawEntity = new GameDrawEntity();
                    gameDrawEntity.setGameId(gameId);
                    gameDrawEntity.setSessionNo(gameSessionEntity.getSessionNo());
                    gameDrawEntity.setSignId(signIds.get(i));
                    gameDrawEntity.setSort(i);
                    gameDrawEntity.setCreatedAt(new Date());
                    result.add(gameDrawEntity);
                }
            }
        }

        drawManager.saveBatch(result);

        return ApiResponse.success(true);
    }

    @Override
    public ApiResponse<?> getDrawList(SystemDrawQuery query) {
        // 查询所有场次
        Long gameId = query.getGameId();
        List<SessionDrawListDao> result = gameDrawMapper.getSessionList(gameId);

        return ApiResponse.success(result);
    }

    /**
     * 查询所有排序内容
     */
    public ApiResponse<?> getDraw(SystemDrawQuery query) {
        // 查询所有场次
        Long gameId = query.getGameId();
        Integer sessionNo = query.getSessionNo();
        List<SessionDrawDao> result = gameDrawMapper.getBySessionNo(gameId, sessionNo);
        List<ArrangeDrawDto> response = new ArrayList<>();
        BeanUtil.copyProperties(result, response);
        return ApiResponse.success(response);
    }

    /**
     * 人工抽签分组
     *
     * @param query
     * @return
     */
    @Override
    public ApiResponse<?> manualDraw(ManualDrawQuery query) {
        Long gameId = query.getGameId();
        if (CollectionUtil.isEmpty(query.getData())) {
            return ApiResponse.error("数据为空");
        }
        // 1、删除所有抽签记录
        LambdaQueryWrapper<GameDrawEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GameDrawEntity::getGameId, gameId);
        queryWrapper.eq(GameDrawEntity::getSessionNo, query.getSessionNo());
        gameDrawMapper.delete(queryWrapper);
        // 2、保存抽签记录
        List<GameDrawEntity> result = new ArrayList<>();
        result = query.getData().stream().map(manualDrawData -> {
            GameDrawEntity drawEntity = new GameDrawEntity();
            drawEntity.setSort(manualDrawData.getSort());
            drawEntity.setGameId(gameId);
            drawEntity.setSessionNo(manualDrawData.getSessionNo());
            drawEntity.setSessionId(manualDrawData.getSessionId());
            drawEntity.setCreatedAt(new Date());
            return drawEntity;
        }).collect(Collectors.toList());
        gameDrawMapper.saveBatch(result);
        return null;
    }

}

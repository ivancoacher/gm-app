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
import com.jsnjwj.facade.query.session.ManualDrawBatchQuery;
import com.jsnjwj.facade.query.session.ManualDrawQuery;
import com.jsnjwj.facade.query.session.SystemDrawQuery;
import com.jsnjwj.facade.service.v2.DrawService;
import com.jsnjwj.facade.vo.session.DrawDetailVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
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

    private final Integer MIN_GAP = 5;

    private final int MAX_POSITIONS = 1000; // 最大位置数

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
            List<GameSessionEntity> sessionList = sessionManager.getListByGameId(query.getGameId());
            Map<Long, Integer> sessionMap = sessionList.stream()
                    .collect(Collectors.toMap(GameSessionEntity::getId, GameSessionEntity::getSessionNo));
            if (CollectionUtil.isEmpty(sessionList)) {
                return ApiResponse.error("请先创建场次");
            }
            LambdaQueryWrapper<GameDrawEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(GameDrawEntity::getGameId, gameId);
            gameDrawMapper.delete(queryWrapper);

            Map<Long, List<Long>> sessionItems = new HashMap<>();
            List<Long> itemAllIds = new ArrayList<>();
            sessionList.forEach(session -> {
                List<Long> itemIds = sessionItemManager.fetchListBySessionId(gameId, session.getId())
                        .stream()
                        .map(GameSessionItemEntity::getItemId)
                        .collect(Collectors.toList());
                sessionItems.put(session.getId(), itemIds);
                itemAllIds.addAll(itemIds);
            });
            Map<Long, List<Long>> itemContestants = new HashMap<>();
            for (Long itemId : itemAllIds) {
                List<SignSingleEntity> signApplyList = signApplyManager.getApplyByItem(gameId, itemId);
                if (CollectionUtil.isEmpty(signApplyList)) {
                    continue;
                }
                List<Long> contestants = signApplyList.stream()
                        .map(SignSingleEntity::getId)
                        .collect(Collectors.toList());
                itemContestants.put(itemId, contestants);
            }
            Map<Long, Set<Integer>> sessionPositions = new HashMap<>();

            // 记录每个选手在每个场次中的位置
            Map<Long, Map<Long, Integer>> contestantPositions = new HashMap<>();

            // 记录每个场次的安排结果
            Map<Long, Map<Integer, Map<Long, Long>>> sessionArrangements = new HashMap<>();

            for (GameSessionEntity session : sessionList) {
                sessionArrangements.put(session.getId(), new HashMap<>());
                sessionPositions.put(session.getId(), new HashSet<>());
            }

            for (Map.Entry<Long, List<Long>> entry : sessionItems.entrySet()) {
                long sessionId = entry.getKey();
                List<Long> projects = entry.getValue();

                System.out.println("\n安排场次 " + sessionId + " 的项目:");

                // 为该场次的每个项目安排选手
                for (Long project : projects) {
                    System.out.println("  项目: " + project);
                    List<Long> contestants = itemContestants.get(project);

                    if (contestants == null || contestants.isEmpty()) {
                        System.out.println("    无选手报名此项目");
                        continue;
                    }

                    // 为每个选手安排位置
                    for (Long contestant : contestants) {
                        // 查找合适的位置
                        int position = findSuitablePosition(contestant, sessionId, contestantPositions,
                                sessionPositions.get(sessionId), MIN_GAP);

                        // 记录安排结果
                        Map<Integer, Map<Long, Long>> sessionArrangement = sessionArrangements.get(sessionId);
                        if (!sessionArrangement.containsKey(position)) {
                            sessionArrangement.put(position, new HashMap<>());
                        }
                        sessionArrangement.get(position).put(project, contestant);

                        // 更新选手位置记录
                        if (!contestantPositions.containsKey(contestant)) {
                            contestantPositions.put(contestant, new HashMap<>());
                        }
                        contestantPositions.get(contestant).put(sessionId, position);

                        GameDrawEntity gameDrawEntity = new GameDrawEntity();
                        gameDrawEntity.setGameId(gameId);
                        gameDrawEntity.setSessionId(sessionId);
                        gameDrawEntity.setSessionNo(sessionMap.get(sessionId));
                        gameDrawEntity.setSignId(contestant);
                        gameDrawEntity.setSort(position);
                        gameDrawEntity.setCreatedAt(new Date());
                        result.add(gameDrawEntity);
                    }
                }
            }

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
                List<Long> signIds = signApplyManagers.stream()
                        .map(SignSingleEntity::getId)
                        .collect(Collectors.toList());
                Collections.shuffle(signIds);
                for (int i = 0; i < signIds.size(); i++) {
                    GameDrawEntity gameDrawEntity = new GameDrawEntity();
                    gameDrawEntity.setGameId(gameId);
                    gameDrawEntity.setSessionNo(gameSessionEntity.getSessionNo());
                    gameDrawEntity.setSignId(signIds.get(i));
                    gameDrawEntity.setSessionId(0L);
                    gameDrawEntity.setSort(i);
                    gameDrawEntity.setCreatedAt(new Date());
                    result.add(gameDrawEntity);
                }
            }
        }
        if (CollectionUtil.isEmpty(result)) {
            return ApiResponse.error("操作失败");
        }
        drawManager.saveBatch(result);

        return ApiResponse.success(true);
    }

    /**
     * 随机按照场次抽签
     */
    private int findSuitablePosition(Long contestant, Long sessionId, Map<Long, Map<Long, Integer>> contestantPositions,
                                     Set<Integer> occupiedPositions,

                                     int minGap) {
        // 获取选手在当前场次已有的位置
        Integer existingPosition = null;
        if (contestantPositions.containsKey(contestant) && contestantPositions.get(contestant).containsKey(sessionId)) {
            existingPosition = contestantPositions.get(contestant).get(sessionId);
            return existingPosition;
        }

        for (int pos = 1; pos <= MAX_POSITIONS; pos++) {
            // 检查位置是否已被占用
            if (occupiedPositions.contains(pos)) {
                continue;
            }

            // 检查是否满足与同一选手其他项目的间隔要求
            if (existingPosition != null && Math.abs(pos - existingPosition) < minGap) {
                continue;
            }

            return pos;
        }

        // 如果选手在此场次没有位置，从位置1开始
        if (existingPosition == null) {
            return 1;
        }

        // 选手已有位置，找一个至少间隔minGap的新位置
        return existingPosition + minGap;
    }

    @Override
    public ApiResponse<?> getDrawList(SystemDrawQuery query) {
        // 查询所有场次
        Long gameId = query.getGameId();
        List<SessionDrawListDao> result = gameDrawMapper.getSessionList(gameId);
        result.forEach(item -> {
            if (item.getSessionNo() == 0) {
                item.setSessionName("默认场次");
            }
        });
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
        DrawDetailVo response = new DrawDetailVo();
        if (0 == sessionNo) {
            response.setSessionNo(0);
            response.setSessionId(0L);
            response.setSessionName("默认场次");
        } else {
            GameSessionEntity session = sessionManager.getBySessionNo(sessionNo);
            response.setSessionNo(session.getSessionNo());
            response.setSessionId(session.getId());
            response.setSessionName(session.getSessionName());
        }
        if (CollectionUtil.isEmpty(result)) {
            return ApiResponse.success(response);
        }
        response.setData(result.stream().map(item -> {
            ArrangeDrawDto dto = new ArrangeDrawDto();
            BeanUtil.copyProperties(item, dto);
            return dto;
        }).collect(Collectors.toList()));
        return ApiResponse.success(response);
    }

    @Override
    public ApiResponse<List<DrawDetailVo>> getAllSessionDraw(SystemDrawQuery query) {
        Long gameId = query.getGameId();
        List<GameSessionEntity> sessionEntities = sessionManager.getListByGameId(gameId);
        List<DrawDetailVo> response = new ArrayList<>();
        if (CollectionUtil.isEmpty(sessionEntities)) {
            return ApiResponse.success(response);
        }

        response = sessionEntities.stream().map(sessionEntity -> {
            Integer sessionNo = sessionEntity.getSessionNo();
            DrawDetailVo drawDetailVo = new DrawDetailVo();

            drawDetailVo.setSessionNo(sessionEntity.getSessionNo());
            drawDetailVo.setSessionId(sessionEntity.getId());
            drawDetailVo.setSessionName(sessionEntity.getSessionName());
            List<SessionDrawDao> result = gameDrawMapper.getBySessionNo(gameId, sessionNo);

            if (CollectionUtil.isEmpty(result)) {
                // 查询这个场次下所有项目对应的报名用户
                List<ArrangeDrawDto> drawList = new ArrayList<>();

                drawDetailVo.setData(new ArrayList<>());
            } else {
                drawDetailVo.setData(result.stream().map(item -> {
                    ArrangeDrawDto dto = new ArrangeDrawDto();
                    BeanUtil.copyProperties(item, dto);
                    return dto;
                }).collect(Collectors.toList()));
            }
            return drawDetailVo;

        }).collect(Collectors.toList());

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
        // LambdaQueryWrapper<GameDrawEntity> queryWrapper = new LambdaQueryWrapper<>();
        // queryWrapper.eq(GameDrawEntity::getGameId, gameId);
        // queryWrapper.eq(GameDrawEntity::getSessionNo, query.getSessionNo());
        // gameDrawMapper.delete(queryWrapper);
        // 2、保存抽签记录
        query.getData().forEach(manualDrawData -> {
            GameDrawEntity gameDrawEntity = gameDrawMapper.selectById(manualDrawData.getDrawId());
            if (!Objects.equals(manualDrawData.getSort(), gameDrawEntity.getSort())) {
                gameDrawEntity.setSort(manualDrawData.getSort());
                gameDrawEntity.setUpdatedAt(new Date());
                gameDrawMapper.updateById(gameDrawEntity);
            }
        });
        return ApiResponse.success();
    }

    @Override
    public ApiResponse<?> manualDrawBatch(ManualDrawBatchQuery query) {
        Long gameId = query.getGameId();
        if (CollectionUtil.isEmpty(query.getData())) {
            return ApiResponse.error("数据为空");
        }
        // 1、删除所有抽签记录
        // LambdaQueryWrapper<GameDrawEntity> queryWrapper = new LambdaQueryWrapper<>();
        // queryWrapper.eq(GameDrawEntity::getGameId, gameId);
        // gameDrawMapper.delete(queryWrapper);
        // 2、保存抽签记录
        query.getData().forEach(manualDrawData -> {
            if (CollectionUtil.isEmpty(manualDrawData.getData())) {
                return;
            }
            manualDrawData.getData().forEach(draw -> {
                GameDrawEntity gameDrawEntity = gameDrawMapper.selectById(draw.getDrawId());
                if (!Objects.equals(draw.getSort(), gameDrawEntity.getSort())) {
                    gameDrawEntity.setSort(draw.getSort());
                    gameDrawEntity.setUpdatedAt(new Date());
                    gameDrawMapper.updateById(gameDrawEntity);
                }
            });
        });
        return ApiResponse.success();
    }

}

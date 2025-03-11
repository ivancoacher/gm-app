package com.jsnjwj.facade.manager;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsnjwj.facade.entity.GameSessionEntity;
import com.jsnjwj.facade.entity.GameSessionItemEntity;
import com.jsnjwj.facade.mapper.GameSessionItemMapper;
import com.jsnjwj.facade.mapper.GameSessionMapper;
import com.jsnjwj.facade.vo.session.SessionItemVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArrangeSessionItemManager {
    private final GameSessionItemMapper gameSessionItemMapper;


    public List<Long> selectAllSelectedItemIds(Long gameId){
        LambdaQueryWrapper<GameSessionItemEntity> sessionItemQuery = new LambdaQueryWrapper<>();
        sessionItemQuery.eq(GameSessionItemEntity::getGameId, gameId);
        List<GameSessionItemEntity> list = gameSessionItemMapper.selectList(sessionItemQuery);
        if (CollectionUtil.isNotEmpty(list)){
            return list.stream().filter(Objects::nonNull).map(GameSessionItemEntity::getItemId).collect(Collectors.toList());
        }else{
            return new ArrayList<>();
        }
    }

    public List<GameSessionItemEntity> fetchListBySessionId(Long gameId, Long sessionId){
        LambdaQueryWrapper<GameSessionItemEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GameSessionItemEntity::getGameId, gameId);
        queryWrapper.eq(GameSessionItemEntity::getSessionId, sessionId);
        return gameSessionItemMapper.selectList(queryWrapper);
    }


    public List<SessionItemVo> fetchBySessionId(Long gameId, Long sessionId){
        return gameSessionItemMapper.fetchBySessionId(gameId,sessionId);
    }


    public void deleteBySessionId(Long gameId, Long sessionId){
        LambdaQueryWrapper<GameSessionItemEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(GameSessionItemEntity::getGameId,gameId);
        lambdaQueryWrapper.eq(GameSessionItemEntity::getSessionId,sessionId);
        gameSessionItemMapper.delete(lambdaQueryWrapper);
    }

    public void saveBatch(List<GameSessionItemEntity> list){
        gameSessionItemMapper.saveBatch(list);
    }
}

package com.jsnjwj.facade.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsnjwj.facade.entity.GameSessionEntity;
import com.jsnjwj.facade.mapper.GameSessionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ArrangeSessionManager {
    private final GameSessionMapper gameSessionMapper;

    public int saveSession(GameSessionEntity query) {
        if (Objects.nonNull(query.getId())){
            return gameSessionMapper.updateById(query);
        }else{
            return gameSessionMapper.insert(query);
        }
    }

    public GameSessionEntity getBySessionId(Long sessionId) {
        LambdaQueryWrapper<GameSessionEntity> query = new LambdaQueryWrapper<>();
        query.eq(GameSessionEntity::getId, sessionId);
        return gameSessionMapper.selectOne(query);
    }

    public GameSessionEntity getBySessionNo(Integer sessionNo) {
        LambdaQueryWrapper<GameSessionEntity> query = new LambdaQueryWrapper<>();
        query.eq(GameSessionEntity::getSessionNo, sessionNo);
        return gameSessionMapper.selectOne(query);
    }

    public void saveSessionBatch(List<GameSessionEntity> list) {
        gameSessionMapper.saveBatch(list);
    }

    public List<GameSessionEntity> getList(Long gameId){
        LambdaQueryWrapper<GameSessionEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GameSessionEntity::getGameId, gameId);
        return gameSessionMapper.selectList(queryWrapper);
    }

    public void resetCourt(Long gameId) {
        LambdaQueryWrapper<GameSessionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GameSessionEntity::getGameId, gameId);
        gameSessionMapper.delete(wrapper);
    }
}

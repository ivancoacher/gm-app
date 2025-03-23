package com.jsnjwj.facade.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsnjwj.facade.dao.SessionDrawListDao;
import com.jsnjwj.facade.entity.GameDrawEntity;
import com.jsnjwj.facade.mapper.GameDrawMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ArrangeDrawManager {

	private final GameDrawMapper drawMapper;

	public void saveBatch(List<GameDrawEntity> list) {
		drawMapper.saveBatch(list);
	}

	/**
	 * 获取已抽签分组场次
	 * @param gameId
	 * @return
	 */
	public List<SessionDrawListDao> getSessionList(Long gameId) {
		return drawMapper.getSessionList(gameId);
	}

	public void clearDraw(Long gameId, Long sessionId) {
		LambdaQueryWrapper<GameDrawEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(GameDrawEntity::getGameId, gameId);
		queryWrapper.eq(Objects.nonNull(sessionId), GameDrawEntity::getSessionId, sessionId);
		drawMapper.delete(queryWrapper);
	}

	public GameDrawEntity getByDrawId(Long gameId, Long sessionId, Long drawId) {
		LambdaQueryWrapper<GameDrawEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(GameDrawEntity::getGameId, gameId);
		queryWrapper.eq(Objects.nonNull(sessionId), GameDrawEntity::getSessionId, sessionId);
		queryWrapper.eq(Objects.nonNull(drawId), GameDrawEntity::getId, drawId);
		return drawMapper.selectOne(queryWrapper);
	}

	public Map<Long, GameDrawEntity> getMapBySessionId(Long gameId, Long sessionId) {
		LambdaQueryWrapper<GameDrawEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(GameDrawEntity::getGameId, gameId);
		queryWrapper.eq(Objects.nonNull(sessionId), GameDrawEntity::getSessionId, sessionId);
		List<GameDrawEntity> result = drawMapper.selectList(queryWrapper);
		return result.stream().collect(java.util.stream.Collectors.toMap(GameDrawEntity::getId, e -> e));
	}

}

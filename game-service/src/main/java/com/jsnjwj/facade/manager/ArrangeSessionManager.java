package com.jsnjwj.facade.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsnjwj.facade.entity.GameSessionEntity;
import com.jsnjwj.facade.entity.GameSessionSettingEntity;
import com.jsnjwj.facade.mapper.GameSessionMapper;
import com.jsnjwj.facade.mapper.GameSessionSettingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ArrangeSessionManager {

	private final GameSessionMapper gameSessionMapper;

	private final GameSessionSettingMapper gameSessionSettingManager;

	public void deleteById(Long gameId, Long sessionId) {
		LambdaQueryWrapper<GameSessionEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(GameSessionEntity::getId, sessionId);
		queryWrapper.eq(GameSessionEntity::getGameId, gameId);
		gameSessionMapper.delete(queryWrapper);
	}

	public int saveSession(GameSessionEntity query) {
		if (Objects.nonNull(query.getId())) {
			return gameSessionMapper.updateById(query);
		}
		else {
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

	public List<GameSessionEntity> getListByGameId(Long gameId) {
		LambdaQueryWrapper<GameSessionEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(GameSessionEntity::getGameId, gameId);
		queryWrapper.orderByAsc(GameSessionEntity::getSessionNo);
		return gameSessionMapper.selectList(queryWrapper);
	}

	public void resetCourt(Long gameId) {
		LambdaQueryWrapper<GameSessionEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(GameSessionEntity::getGameId, gameId);
		gameSessionMapper.delete(wrapper);
	}

	public GameSessionSettingEntity getSettingBySessionId(Long gameId, Long sessionId) {
		LambdaQueryWrapper<GameSessionSettingEntity> query = new LambdaQueryWrapper<>();
		query.eq(GameSessionSettingEntity::getGameId, gameId);
		query.eq(GameSessionSettingEntity::getSessionId, sessionId);
		return gameSessionSettingManager.selectOne(query);
	}

	public int updateSettingBySessionId(Long gameId, Long sessionId, GameSessionSettingEntity settingEntity) {
		LambdaQueryWrapper<GameSessionSettingEntity> query = new LambdaQueryWrapper<>();
		query.eq(GameSessionSettingEntity::getGameId, gameId);
		query.eq(GameSessionSettingEntity::getSessionId, sessionId);
		return gameSessionSettingManager.update(settingEntity, query);
	}

	public int saveSessionSetting(GameSessionSettingEntity settingEntity) {
		if (Objects.nonNull(settingEntity.getId())) {
			settingEntity.setUpdatedAt(new Date());
			return gameSessionSettingManager.updateById(settingEntity);
		}
		return gameSessionSettingManager.insert(settingEntity);
	}

}

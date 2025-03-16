package com.jsnjwj.facade.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsnjwj.facade.dao.SessionDrawListDao;
import com.jsnjwj.facade.entity.GameDrawEntity;
import com.jsnjwj.facade.entity.GameSessionEntity;
import com.jsnjwj.facade.mapper.GameDrawMapper;
import com.jsnjwj.facade.mapper.GameSessionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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
	public List<SessionDrawListDao> getSessionList(Long gameId){
		return drawMapper.getSessionList(gameId);
	}
}

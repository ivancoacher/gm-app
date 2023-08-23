package com.jsnjwj.facade.manager;

import com.jsnjwj.facade.entity.TcGames;
import com.jsnjwj.facade.enums.GameStatusEnum;
import com.jsnjwj.facade.mapper.TcGamesMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class GameManager {

	@Resource
	private TcGamesMapper tcGamesMapper;

	public int save(TcGames games) {
		games.setStatus(GameStatusEnum.GAME_PROJECTED.getCode());
		games.setCreateTime(new Date());
		games.setUpdateTime(new Date());
		return tcGamesMapper.insert(games);
	}

	public int update(TcGames games) {
		return tcGamesMapper.updateById(games);
	}

	public TcGames fetchInfo(Long gameId) {
		return tcGamesMapper.selectById(gameId);
	}

}

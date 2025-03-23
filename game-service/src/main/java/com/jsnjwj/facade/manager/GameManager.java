package com.jsnjwj.facade.manager;

import com.jsnjwj.facade.entity.GamesEntity;
import com.jsnjwj.facade.enums.GameStatusEnum;
import com.jsnjwj.facade.mapper.GamesMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class GameManager {

	@Resource
	private GamesMapper gamesMapper;

	public int save(GamesEntity games) {
		games.setStatus(GameStatusEnum.GAME_PROJECTED.getCode());
		games.setCreateTime(new Date());
		games.setUpdateTime(new Date());
		return gamesMapper.insert(games);
	}

	public int update(GamesEntity games) {
		return gamesMapper.updateById(games);
	}

	public GamesEntity fetchInfo(Long gameId) {
		return gamesMapper.selectById(gameId);
	}

}

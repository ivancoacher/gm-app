package com.jsnjwj.facade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.entity.TcGames;
import com.jsnjwj.facade.enums.GameStatusEnum;
import com.jsnjwj.facade.mapper.TcGamesMapper;
import com.jsnjwj.facade.query.GameInfoQuery;
import com.jsnjwj.facade.query.GameListQuery;
import com.jsnjwj.facade.vo.GameInfoVo;
import com.jsnjwj.facade.service.GameInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 合同比对service
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class GameInfoInfoServiceImpl implements GameInfoService {

	@Resource
	private TcGamesMapper gameMapper;

	@Override
	public ApiResponse<Page<GameInfoVo>> queryList(GameListQuery query) {
		Page<GameInfoVo> response = new Page<>();

		Page<TcGames> page = new Page<>();
		LambdaQueryWrapper<TcGames> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(StringUtils.isNotEmpty(query.getGameName()), TcGames::getGameName, query.getGameName());
		page = gameMapper.selectPage(page, wrapper);

		return ApiResponse.success(response);
	}

	@Override
	public ApiResponse<TcGames> fetchInfo(GameInfoQuery query) {
		GameInfoVo response = new GameInfoVo();

		TcGames gameInfo = gameMapper.selectById(query.getGameId());
		if (gameInfo != null) {
			response.setId(gameInfo.getId());
			response.setGameName(gameInfo.getGameName());
			response.setStatus(gameInfo.getStatus());
			response.setStatusDesc(GameStatusEnum.getValue(gameInfo.getStatus()));
			response.setSignStartTime(gameInfo.getSignStartTime());
			response.setSignEndTime(gameInfo.getSignEndTime());
			response.setGameTime(gameInfo.getGameTime());
			response.setCreateTime(gameInfo.getCreateTime());
			response.setUpdateTime(gameInfo.getUpdateTime());

		}
		return ApiResponse.success(response);
	}

	@Override
	public ApiResponse<Boolean> update(GameInfoQuery query) {
		TcGames games = new TcGames();
		games.setId(query.getGameId());
		games.setGameName(query.getGameName());
		int result = gameMapper.insert(games);
		return ApiResponse.success(result > 0);
	}

	@Override
	public ApiResponse<Boolean> save(GameInfoQuery query) {
		TcGames games = new TcGames();
		int result = gameMapper.insert(games);
		return ApiResponse.success(result > 0);
	}

	@Override
	public ApiResponse<Boolean> changeStatus(GameInfoQuery query) {
		TcGames games = new TcGames();
		games.setId(query.getGameId());
		games.setStatus(query.getGameStatus());
		int result = gameMapper.updateById(games);
		return ApiResponse.success(result > 0);
	}

}

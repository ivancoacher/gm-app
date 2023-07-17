package com.jsnjwj.facade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.entity.TcGames;
import com.jsnjwj.facade.enums.GameStatusEnum;
import com.jsnjwj.facade.manager.GameManager;
import com.jsnjwj.facade.mapper.TcGamesMapper;
import com.jsnjwj.facade.query.GameAddQuery;
import com.jsnjwj.facade.query.GameInfoQuery;
import com.jsnjwj.facade.query.GameListQuery;
import com.jsnjwj.facade.query.GameModifyQuery;
import com.jsnjwj.facade.service.GameInfoService;
import com.jsnjwj.facade.vo.GameInfoVo;
import com.jsnjwj.facade.vo.GameListVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 合同比对service
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class GameInfoInfoServiceImpl implements GameInfoService {

	@Resource
	private TcGamesMapper gameMapper;

	@Resource
	private GameManager gameManager;

	@Override
	public ApiResponse<GameListVo> queryList(GameListQuery query) {

		Page<TcGames> page = new Page<>();
		LambdaQueryWrapper<TcGames> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(StringUtils.isNotEmpty(query.getGameName()), TcGames::getGameName, query.getGameName());
		page = gameMapper.selectPage(page, wrapper);

		GameListVo gameListVo = new GameListVo();
		gameListVo.setCurrent(page.getCurrent());
		gameListVo.setPages(page.getPages());
		gameListVo.setCurrent(page.getCurrent());
		gameListVo.setTotal(page.getTotal());
		gameListVo.setSize(page.getSize());
		List<GameInfoVo> records = new ArrayList<>();
		if (!page.getRecords().isEmpty()) {
			page.getRecords().forEach(rec -> {
				GameInfoVo infoVo = new GameInfoVo();
				infoVo.setGameId(rec.getId());
				infoVo.setGameName(rec.getGameName());
				infoVo.setStatus(rec.getStatus());
				infoVo.setStatusDesc(GameStatusEnum.getValue(rec.getStatus()));
				infoVo.setSignStartTime(rec.getSignStartTime());
				infoVo.setSignEndTime(rec.getSignEndTime());
				infoVo.setGameStartTime(rec.getGameStartTime());
				infoVo.setGameEndTime(rec.getGameEndTime());
				infoVo.setGameLocation(rec.getGameLocation());
				infoVo.setGameType(rec.getGameType());
				infoVo.setSignType(rec.getSignType());
				infoVo.setUpdateTime(rec.getUpdateTime());
				records.add(infoVo);
			});
		}
		gameListVo.setRecords(records);
		return ApiResponse.success(gameListVo);
	}

	@Override
	public ApiResponse<TcGames> fetchInfo(GameInfoQuery query) {
		GameInfoVo response = new GameInfoVo();

		TcGames gameInfo = gameMapper.selectById(query.getGameId());
		if (gameInfo != null) {
			response.setGameId(gameInfo.getId());
			response.setGameName(gameInfo.getGameName());
			response.setStatus(gameInfo.getStatus());
			response.setStatusDesc(GameStatusEnum.getValue(gameInfo.getStatus()));
			response.setSignStartTime(gameInfo.getSignStartTime());
			response.setSignEndTime(gameInfo.getSignEndTime());
			response.setGameStartTime(gameInfo.getGameStartTime());
			response.setGameEndTime(gameInfo.getGameEndTime());
			response.setGameLocation(gameInfo.getGameLocation());
			response.setGameType(gameInfo.getGameType());
			response.setSignType(gameInfo.getSignType());
			return ApiResponse.success(response);
		}
		return ApiResponse.error("赛事信息不存在");
	}

	@Override
	public ApiResponse<Boolean> update(GameModifyQuery query) {
		TcGames games = new TcGames();
		games.setId(query.getGameId());
		games.setGameName(query.getGameName());
		games.setSignType(query.getSignType());
		games.setSignStartTime(query.getSignStartTime());
		games.setSignEndTime(query.getSignEndTime());
		games.setGameStartTime(query.getGameStartTime());
		games.setGameEndTime(query.getGameEndTime());
		games.setCreatorId(query.getUserId());
		games.setGameType(query.getGameType());
		games.setGameLocation(query.getGameLocation());
		int result = gameManager.update(games);
		return ApiResponse.success(result > 0);
	}

	@Override
	public ApiResponse<Boolean> save(GameAddQuery query) {
		TcGames games = new TcGames();
		games.setGameName(query.getGameName());
		games.setSignType(query.getSignType());
		games.setSignStartTime(query.getSignStartTime());
		games.setSignEndTime(query.getSignEndTime());
		games.setGameStartTime(query.getGameStartTime());
		games.setGameEndTime(query.getGameEndTime());
		games.setCreatorId(query.getUserId());
		games.setGameType(query.getGameType());
		games.setGameLocation(query.getGameLocation());
		int result = gameManager.save(games);
		return ApiResponse.success(result > 0);
	}

	@Override
	public ApiResponse<Boolean> changeStatus(GameModifyQuery query) {
		TcGames games = new TcGames();
		games.setId(query.getGameId());
		games.setStatus(query.getGameStatus());
		int result = gameManager.update(games);
		return ApiResponse.success(result > 0);
	}

}

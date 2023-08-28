package com.jsnjwj.api.controller;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.common.utils.ThreadLocalUtil;
import com.jsnjwj.facade.entity.GamesEntity;
import com.jsnjwj.facade.query.GameAddQuery;
import com.jsnjwj.facade.query.GameInfoQuery;
import com.jsnjwj.facade.query.GameListQuery;
import com.jsnjwj.facade.query.GameModifyQuery;
import com.jsnjwj.facade.service.GameInfoService;
import com.jsnjwj.facade.vo.GameListVo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/game")
public class GameController {

	@Resource
	private GameInfoService gameInfoService;

	@RequestMapping(value = "/list")
	public ApiResponse<GameListVo> list(GameListQuery query, HttpServletRequest request) {
		query.setUserId(ThreadLocalUtil.getCurrentUserId());
		return gameInfoService.queryList(query);
	}

	@RequestMapping(value = "/info")
	public ApiResponse<GamesEntity> info(GameInfoQuery query, HttpServletRequest request) {
		query.setUserId(ThreadLocalUtil.getCurrentUserId());
		return gameInfoService.fetchInfo(query);
	}

	@RequestMapping(value = "/update")
	public ApiResponse<Boolean> update(@RequestBody GameModifyQuery query, HttpServletRequest request) {
		query.setUserId(ThreadLocalUtil.getCurrentUserId());
		return gameInfoService.update(query);
	}

	@RequestMapping(value = "/save")
	public ApiResponse<Boolean> save(@RequestBody GameAddQuery query, HttpServletRequest request) {
		query.setUserId(ThreadLocalUtil.getCurrentUserId());
		return gameInfoService.save(query);
	}

	@RequestMapping(value = "/status/switch")
	public ApiResponse<Boolean> changeStatus(@RequestBody GameModifyQuery query, HttpServletRequest request) {
		query.setUserId(ThreadLocalUtil.getCurrentUserId());
		return gameInfoService.changeStatus(query);
	}

}

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

/**
 * 赛事信息管理
 */
@RestController
@RequestMapping(value = "/game")
public class GameController {

	@Resource
	private GameInfoService gameInfoService;

	/**
	 * 赛事列表
	 * @param query GameListQuery
	 * @return ApiResponse
	 */
	@RequestMapping(value = "/list")
	public ApiResponse<GameListVo> list(GameListQuery query) {
		query.setUserId(ThreadLocalUtil.getCurrentUserId());
		return gameInfoService.queryList(query);
	}

	/**
	 * 赛事详情
	 * @param query GameInfoQuery
	 * @return ApiResponse
	 */
	@RequestMapping(value = "/info")
	public ApiResponse<GamesEntity> info(GameInfoQuery query) {
		query.setUserId(ThreadLocalUtil.getCurrentUserId());
		return gameInfoService.fetchInfo(query);
	}

	/**
	 * 赛事信息更新
	 * @param query GameModifyQuery
	 * @return ApiResponse
	 */
	@RequestMapping(value = "/update")
	public ApiResponse<Boolean> update(@RequestBody GameModifyQuery query) {
		query.setUserId(ThreadLocalUtil.getCurrentUserId());
		return gameInfoService.update(query);
	}

	/**
	 * 赛事新增
	 * @param query GameAddQuery
	 * @return ApiResponse
	 */
	@RequestMapping(value = "/save")
	public ApiResponse<Boolean> save(@RequestBody GameAddQuery query) {
		query.setUserId(ThreadLocalUtil.getCurrentUserId());
		return gameInfoService.save(query);
	}

	/**
	 * 赛事状态调整
	 * @param query GameModifyQuery
	 * @return ApiResponse
	 */
	@RequestMapping(value = "/status/switch")
	public ApiResponse<Boolean> changeStatus(@RequestBody GameModifyQuery query) {
		query.setUserId(ThreadLocalUtil.getCurrentUserId());
		return gameInfoService.changeStatus(query);
	}

}

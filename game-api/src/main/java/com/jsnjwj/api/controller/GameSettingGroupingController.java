package com.jsnjwj.api.controller;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.query.GameGroupingViewQuery;
import com.jsnjwj.facade.service.GameGroupingService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 项目编排配置
 */
@RestController
@RequestMapping("/game/setting/grouping")
public class GameSettingGroupingController {

	@Resource
	private GameGroupingService gameGroupingService;

	@GetMapping("/index")
	public ApiResponse<?> queryList(GameGroupingViewQuery query) {
		return ApiResponse.success(gameGroupingService.fetchGroupingItem(query));
	}

	@GetMapping("/update")
	public ApiResponse<?> update(GameGroupingViewQuery query) {
		return ApiResponse.success(gameGroupingService.fetchGroupingItem(query));
	}

	@GetMapping("/detail")
	public ApiResponse<?> queryDetail(GameGroupingViewQuery query) {
		return ApiResponse.success(gameGroupingService.fetchGroupingDetail(query));
	}

	@PostMapping("/set")
	public ApiResponse<?> setGrouping() {
		return ApiResponse.success();
	}

	@PostMapping("/setBatch")
	public ApiResponse<?> setGroupingBatch() {
		return ApiResponse.success();
	}

}

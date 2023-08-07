package com.jsnjwj.api.controller;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.entity.TcGameArea;
import com.jsnjwj.facade.query.GameGroupingSetNumQuery;
import com.jsnjwj.facade.query.GameGroupingAreaSetQuery;
import com.jsnjwj.facade.query.GameGroupingSetQuery;
import com.jsnjwj.facade.service.GameGroupingService;
import com.jsnjwj.facade.service.GameSettingService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户-组别编排
 */
@RestController
@RequestMapping("/game/setting/grouping")
public class GameSettingGroupingController {

	@Resource
	private GameGroupingService gameGroupingService;

	@RequestMapping("/index")
	public ApiResponse<?> queryList() {
		return ApiResponse.success();
	}

	@RequestMapping("/set")
	public ApiResponse<?> setGrouping() {
		return ApiResponse.success();
	}

}

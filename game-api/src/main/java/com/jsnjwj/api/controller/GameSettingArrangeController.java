package com.jsnjwj.api.controller;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.entity.TcGameArea;
import com.jsnjwj.facade.query.GameGroupingAreaSetQuery;
import com.jsnjwj.facade.query.GameGroupingListQuery;
import com.jsnjwj.facade.query.GameGroupingSetNumQuery;
import com.jsnjwj.facade.query.GameGroupingSetQuery;
import com.jsnjwj.facade.service.GameSettingService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 项目分组
 */
@RestController
@RequestMapping("/game/setting/arrange")
public class GameSettingArrangeController {

	@Resource
	private GameSettingService gameSettingService;

	/**
	 * 获取分组详情 项目-场地分组信息
	 * @param query
	 * @return
	 */
	@GetMapping("/grouping/list")
	public ApiResponse<?> fetchArrangeList(GameGroupingListQuery query) {
		return gameSettingService.fetchArrangeList(query);
	}

	/**
	 * 批量设置分组
	 * @param query
	 * @return
	 */
	@PostMapping("/grouping/batch")
	public ApiResponse<Boolean> setGroupingBatch(@RequestBody GameGroupingSetQuery query) {
		return gameSettingService.setGroupingBatch(query);
	}

	/**
	 * 设置分类分组
	 * @param query
	 * @return
	 */
	@PostMapping("/grouping/item")
	public ApiResponse<Boolean> setGroupingItem(@RequestBody GameGroupingSetQuery query) {
		return gameSettingService.setGrouping(query);
	}

}

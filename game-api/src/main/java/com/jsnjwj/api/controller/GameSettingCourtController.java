package com.jsnjwj.api.controller;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.entity.TcGameArea;
import com.jsnjwj.facade.query.GameGroupingAreaSetQuery;
import com.jsnjwj.facade.query.GameGroupingListQuery;
import com.jsnjwj.facade.query.GameGroupingSetNumQuery;
import com.jsnjwj.facade.query.GameGroupingSetQuery;
import com.jsnjwj.facade.service.GameArrangeService;
import com.jsnjwj.facade.service.GameSettingService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 项目分组
 */
@RestController
@RequestMapping("/game/setting/arrange/area")
public class GameSettingCourtController {

	@Resource
	private GameArrangeService gameArrangeService;

	/**
	 * 场地列表
	 * @param gameId
	 * @return
	 */
	@GetMapping("/list")
	public ApiResponse<List<TcGameArea>> getAreas(@RequestParam("gameId") Long gameId) {
		return gameArrangeService.getCourts(gameId);
	}

	/**
	 * 设置场地数量
	 * @param query
	 * @return
	 */
	@PostMapping("/setNum")
	public ApiResponse<?> setAreaNum(@RequestBody GameGroupingSetNumQuery query) {
		return gameArrangeService.setCourtNum(query);
	}

	/**
	 * 修改场地信息
	 * @param query
	 * @return
	 */
	@PostMapping("/update")
	public ApiResponse<Boolean> saveArea(@RequestBody GameGroupingAreaSetQuery query) {
		return gameArrangeService.saveCourt(query);
	}

}

package com.jsnjwj.api.controller;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.entity.TcGameArea;
import com.jsnjwj.facade.query.GameGroupingAreaSetQuery;
import com.jsnjwj.facade.query.GameGroupingSetNumQuery;
import com.jsnjwj.facade.query.GameGroupingSetQuery;
import com.jsnjwj.facade.service.GameSettingService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/game/setting/rule")
public class GameSettingRuleController {

	@Resource
	private GameSettingService gameSettingService;

	@GetMapping("/setRule")
	public ApiResponse<List<TcGameArea>> getAreas(@RequestParam("gameId") Long gameId) {
		return gameSettingService.getCourts(gameId);
	}

	@PostMapping("/getRule")
	public ApiResponse<Boolean> setAreaNum(@RequestBody GameGroupingSetNumQuery query) {
		return gameSettingService.setCourtNum(query);
	}

}

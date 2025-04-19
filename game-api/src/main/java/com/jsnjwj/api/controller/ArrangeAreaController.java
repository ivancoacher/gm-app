package com.jsnjwj.api.controller;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.common.utils.ThreadLocalUtil;
import com.jsnjwj.facade.dto.AreaSessionDto;
import com.jsnjwj.facade.dto.ArrangeAreaSessionDto;
import com.jsnjwj.facade.dto.SessionChooseDto;
import com.jsnjwj.facade.query.GameGroupingAreaSetQuery;
import com.jsnjwj.facade.query.GameGroupingSetNumQuery;
import com.jsnjwj.facade.query.ManualDrawAreaSessionBatchQuery;
import com.jsnjwj.facade.service.v2.ArrangeAreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 项目分组
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/game/setting/arrange/area")
public class ArrangeAreaController {

	private final ArrangeAreaService arrangeAreaService;

	/**
	 * 场地列表
	 * @return
	 */
	@GetMapping("/list")
	public ApiResponse<List<ArrangeAreaSessionDto>> getAreas() {
		Long gameId = ThreadLocalUtil.getCurrentGameId();
		return arrangeAreaService.getAreas(gameId);
	}

	/**
	 * 设置场地数量
	 * @param query
	 * @return
	 */
	@PostMapping("/setNum")
	public ApiResponse<?> setAreaNum(@RequestBody GameGroupingSetNumQuery query) {
		query.setGameId(ThreadLocalUtil.getCurrentGameId());
		return arrangeAreaService.setAreaNum(query);
	}

	/**
	 * 修改场地信息
	 * @param query
	 * @return
	 */
	@PostMapping("/update")
	public ApiResponse<Boolean> saveArea(@RequestBody GameGroupingAreaSetQuery query) {
		query.setGameId(ThreadLocalUtil.getCurrentGameId());
		return arrangeAreaService.saveArea(query);
	}

	/**
	 * 可选择场次
	 * @param query
	 * @return
	 */
	@PostMapping("/session/select")
	public ApiResponse<List<AreaSessionDto>> selectSession(@RequestBody GameGroupingAreaSetQuery query) {
		query.setGameId(ThreadLocalUtil.getCurrentGameId());
		return arrangeAreaService.selectSessionList(query);
	}

	@PostMapping("/session/unSelect")
	public ApiResponse<AreaSessionDto> selectUnSession(@RequestBody GameGroupingAreaSetQuery query) {
		query.setGameId(ThreadLocalUtil.getCurrentGameId());
		return arrangeAreaService.selectUnSessionList(query);
	}

	@PostMapping("/session/random")
	public ApiResponse<Boolean> arrangeRandom() {
		ManualDrawAreaSessionBatchQuery query = new ManualDrawAreaSessionBatchQuery();
		query.setGameId(ThreadLocalUtil.getCurrentGameId());
		return arrangeAreaService.arrangeSessionRandom(query);
	}

	@PostMapping("/session/save")
	public ApiResponse<Boolean> arrangeSave(@RequestBody ManualDrawAreaSessionBatchQuery query) {
		query.setGameId(ThreadLocalUtil.getCurrentGameId());
		return arrangeAreaService.arrangeSessionSave(query);
	}

}

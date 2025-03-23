package com.jsnjwj.api.controller;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.common.utils.ThreadLocalUtil;
import com.jsnjwj.facade.entity.GameSessionEntity;
import com.jsnjwj.facade.query.session.*;
import com.jsnjwj.facade.service.v2.ArrangeSessionItemService;
import com.jsnjwj.facade.service.v2.ArrangeSessionService;
import com.jsnjwj.facade.service.v2.DrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 抽签分组
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/game/setting/arrange/draw")
public class ArrangeDrawController {

	private final DrawService drawService;

	/**
	 * 单场次随机抽签
	 * @return
	 */
	@PostMapping("/system/single")
	public ApiResponse<?> systemDrawSingle(@RequestBody SystemDrawQuery query) {
		Long gameId = ThreadLocalUtil.getCurrentGameId();
		query.setGameId(gameId);
		return drawService.systemDrawSingle(query);
	}

	/**
	 * 全部场次随机抽签
	 * @param query
	 * @return
	 */
	@PostMapping("/system/random")
	public ApiResponse<?> systemDrawRandom(@RequestBody SystemDrawQuery query) {
		Long gameId = ThreadLocalUtil.getCurrentGameId();
		query.setGameId(gameId);
		return drawService.systemDrawRandom(query);
	}

	@PostMapping("/list")
	public ApiResponse<?> getDrawList() {
		SystemDrawQuery query = new SystemDrawQuery();
		Long gameId = ThreadLocalUtil.getCurrentGameId();
		query.setGameId(gameId);
		return drawService.getDrawList(query);
	}

	/**
	 * 获取单个场次的编排数据
	 * @param query
	 * @return
	 */
	@PostMapping("/bySession")
	public ApiResponse<?> getSessionDraw(@RequestBody SystemDrawQuery query) {
		Long gameId = ThreadLocalUtil.getCurrentGameId();
		query.setGameId(gameId);
		return drawService.getDraw(query);
	}

	/**
	 * 查询所有场次的编排数据
	 * @param query
	 * @return
	 */
	@PostMapping("/bySessions")
	public ApiResponse<?> getSessionsDraw(@RequestBody SystemDrawQuery query) {
		Long gameId = ThreadLocalUtil.getCurrentGameId();
		query.setGameId(gameId);
		return drawService.getAllSessionDraw(query);
	}

	/**
	 * 手动编排设计 调整编排顺序
	 * @param query
	 * @return
	 */
	@PostMapping("/manual")
	public ApiResponse<?> manualDraw(@RequestBody ManualDrawQuery query) {
		query.setGameId(ThreadLocalUtil.getCurrentGameId());
		return drawService.manualDraw(query);
	}

	/**
	 * 保存全部抽签分组数据
	 * @param query
	 * @return
	 */
	@PostMapping("/manual/batch")
	public ApiResponse<?> manualDrawBatch(@RequestBody ManualDrawBatchQuery query) {
		query.setGameId(ThreadLocalUtil.getCurrentGameId());
		return drawService.manualDrawBatch(query);
	}

}

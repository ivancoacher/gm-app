package com.jsnjwj.api.controller;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.common.utils.ThreadLocalUtil;
import com.jsnjwj.facade.dto.ArrangeSessionInfoDto;
import com.jsnjwj.facade.dto.ArrangeSessionVo;
import com.jsnjwj.facade.entity.GameSessionEntity;
import com.jsnjwj.facade.query.session.*;
import com.jsnjwj.facade.service.v2.ArrangeSessionItemService;
import com.jsnjwj.facade.service.v2.ArrangeSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 项目分组
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/game/setting/arrange/session")
public class ArrangeSessionController {

	private final ArrangeSessionService arrangeSessionService;

	private final ArrangeSessionItemService arrangeSessionItemService;

	/**
	 * 场地列表
	 * @return
	 */
	@GetMapping("/list")
	public ApiResponse<List<ArrangeSessionVo>> getSessions() {
		Long gameId = ThreadLocalUtil.getCurrentGameId();
		return arrangeSessionService.getSessions(gameId);
	}

	/**
	 * 项目分配统计
	 * @return
	 */
	@GetMapping("/info")
	public ApiResponse<ArrangeSessionInfoDto> getSessionInfo() {
		Long gameId = ThreadLocalUtil.getCurrentGameId();
		return arrangeSessionService.getSessionInfo(gameId);
	}

	/**
	 * 设置场地数量
	 * @param query
	 * @return
	 */
	@PostMapping("/setNum")
	public ApiResponse<?> setSessionNum(@RequestBody GameGroupingSessionSetNumQuery query) {
		query.setGameId(ThreadLocalUtil.getCurrentGameId());
		return arrangeSessionService.setSessionNum(query);
	}

	/**
	 * 新增场次
	 * @return
	 */
	@PostMapping("/add")
	public ApiResponse<?> addSession() {
		return arrangeSessionService.addSession(ThreadLocalUtil.getCurrentGameId());
	}

	/**
	 * 删除场次
	 * @param query
	 * @return
	 */
	@PostMapping("/delete")
	public ApiResponse<?> deleteSession(@RequestBody GameGroupingSessionSetQuery query) {
		query.setGameId(ThreadLocalUtil.getCurrentGameId());
		return arrangeSessionService.deleteSession(query);
	}

	/**
	 * 修改场地信息
	 * @param query
	 */
	@PostMapping("/update")
	public ApiResponse<Boolean> saveSession(@RequestBody GameGroupingSessionSetQuery query) {
		query.setGameId(ThreadLocalUtil.getCurrentGameId());
		return arrangeSessionService.saveSession(query);
	}

	/**
	 * 剩余未选场次项目
	 * @return
	 */
	@GetMapping("/item/unSelected")
	public ApiResponse<?> getUnSelected(@RequestParam(value = "groupId", required = false) Long groupId,
			@RequestParam(value = "itemId", required = false) Long itemId) {
		SessionItemGetQuery query = new SessionItemGetQuery();
		query.setGameId(ThreadLocalUtil.getCurrentGameId());
		query.setGroupId(groupId);
		query.setItemId(itemId);
		return arrangeSessionItemService.getUnSelectedItem(query);
	}

	/**
	 * 查询该场次内的已选项目
	 * @param sessionId
	 * @return
	 */
	@GetMapping("/item/selected")
	public ApiResponse<?> getSelected(@RequestParam("sessionId") Long sessionId) {
		SessionItemGetQuery query = new SessionItemGetQuery();
		query.setSessionId(sessionId);
		query.setGameId(ThreadLocalUtil.getCurrentGameId());
		return arrangeSessionItemService.getSelectedItem(query);
	}

	@GetMapping("/item/selected/list")
	public ApiResponse<?> getSelectedList() {
		SessionItemGetQuery query = new SessionItemGetQuery();
		query.setGameId(ThreadLocalUtil.getCurrentGameId());
		return arrangeSessionItemService.getSelectedItemList(query);
	}

	@PostMapping("/item/save")
	public ApiResponse<Boolean> saveItem(@RequestBody SessionItemSetQuery query) {
		query.setGameId(ThreadLocalUtil.getCurrentGameId());
		return arrangeSessionItemService.saveSessionItem(query);
	}

	@PostMapping("/item/save/batch")
	public ApiResponse<Boolean> saveItemBatch(@RequestBody SessionItemSetBatchQuery query) {
		query.setGameId(ThreadLocalUtil.getCurrentGameId());
		return arrangeSessionItemService.saveSessionItemBatch(query);
	}

	@PostMapping("/item/save/random")
	public ApiResponse<Boolean> saveItemRandom() {
		SessionItemSetBatchQuery query = new SessionItemSetBatchQuery();
		query.setGameId(ThreadLocalUtil.getCurrentGameId());
		return arrangeSessionItemService.saveSessionItemRandom(query);
	}

}

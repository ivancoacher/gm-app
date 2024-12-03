package com.jsnjwj.api.controller;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.common.utils.ThreadLocalUtil;
import com.jsnjwj.facade.query.GameSettingSetRulesQuery;
import com.jsnjwj.facade.service.GameArrangeService;
import com.jsnjwj.facade.service.GameSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequiredArgsConstructor
@RequestMapping("/game/setting/rule")
public class GameSettingRuleController {

	private final GameSettingService gameSettingService;

	private final GameArrangeService gameArrangeService;

	/**
	 * 获取项目规则列表
	 * @param query
	 * @return
	 */
	@GetMapping("/getItemRuleList")
	public ApiResponse<?> getItemRuleList(@RequestBody GameSettingSetRulesQuery query) {
		query.setGameId(ThreadLocalUtil.getCurrentGameId());
		query.setUserId(ThreadLocalUtil.getCurrentUserId());
		return gameArrangeService.getItemRules(query);
	}

	/**
	 * 设置项目规则
	 * @param query
	 * @return
	 */
	@GetMapping("/setItemRule")
	public ApiResponse<?> setItemRule(@RequestBody GameSettingSetRulesQuery query) {
		query.setGameId(ThreadLocalUtil.getCurrentGameId());
		query.setUserId(ThreadLocalUtil.getCurrentUserId());
		return gameArrangeService.setItemRules(query);
	}

	/**
	 * 获取项目规则详情
	 * @param query
	 * @return
	 */
	@GetMapping("/getItemRuleDetail")
	public ApiResponse<?> getItemRuleDetail(@RequestBody GameSettingSetRulesQuery query) {
		query.setGameId(ThreadLocalUtil.getCurrentGameId());
		query.setUserId(ThreadLocalUtil.getCurrentUserId());
		return gameArrangeService.getItemRulesDetail(query);
	}

	/**
	 * 设置规则
	 * @param query
	 * @return
	 */
	@GetMapping("/setRule")
	public ApiResponse<?> setRule(@RequestBody GameSettingSetRulesQuery query) {
		query.setGameId(ThreadLocalUtil.getCurrentGameId());
		query.setUserId(ThreadLocalUtil.getCurrentUserId());
		return gameSettingService.setRules(query);
	}

	/**
	 * 获取规则
	 * @param gameId
	 * @param itemId
	 * @return
	 */
	@PostMapping("/getRuleList")
	public ApiResponse<?> getRule(@RequestParam("gameId") Long gameId, @RequestParam("itemId") Long itemId) {
		return gameSettingService.getRules(gameId, itemId);
	}

}

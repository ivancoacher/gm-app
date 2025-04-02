package com.jsnjwj.facade.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.entity.GameItemEntity;
import com.jsnjwj.facade.entity.GameItemRule;
import com.jsnjwj.facade.enums.ItemTypeEnum;
import com.jsnjwj.facade.enums.SettingRuleEnum;
import com.jsnjwj.facade.manager.GameGroupManager;
import com.jsnjwj.facade.manager.GameItemManager;
import com.jsnjwj.facade.manager.GameItemRuleManager;
import com.jsnjwj.facade.mapper.GameItemRuleMapper;
import com.jsnjwj.facade.query.GameItemListQuery;
import com.jsnjwj.facade.query.rule.ItemRuleQuery;
import com.jsnjwj.facade.query.rule.ItemRuleSetQuery;
import com.jsnjwj.facade.service.GameItemRuleSetService;
import com.jsnjwj.facade.vo.ItemLabelVo;
import com.jsnjwj.facade.vo.rule.GameItemRuleVo;
import com.jsnjwj.facade.vo.rule.RuleVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameItemRuleSetServiceImpl implements GameItemRuleSetService {

	private final GameItemRuleMapper gameItemRuleMapper;

	private final GameItemManager gameItemManager;

	private final GameItemRuleManager gameItemRuleManager;

	private final GameGroupManager gameGroupManager;

	/**
	 * 获取所有项目对应的比赛规则
	 * @param query
	 * @return
	 */
	@Override
	public ApiResponse<List<GameItemRuleVo>> getItemRules(ItemRuleQuery query) {

		GameItemListQuery gameItemListQuery = new GameItemListQuery();
		gameItemListQuery.setGameId(query.getGameId());
		gameItemListQuery.setGroupId(query.getGroupId());
		gameItemListQuery.setPage(query.getPage());
		gameItemListQuery.setLimit(query.getLimit());
		Page<ItemLabelVo> itemEntities = gameItemManager.fetchItemsPage(gameItemListQuery);
		if (CollectionUtil.isEmpty(itemEntities.getRecords())) {
			return ApiResponse.success(Collections.emptyList());
		}
		// 获取所有小项
		Page<GameItemRuleVo> ruleResult = new Page<>();
		LambdaQueryWrapper<GameItemRule> ruleQueryWrapper = new LambdaQueryWrapper<>();
		ruleQueryWrapper.eq(GameItemRule::getGameId, query.getGameId());
		ruleQueryWrapper.in(GameItemRule::getItemId,
				itemEntities.getRecords().stream().map(ItemLabelVo::getItemId).collect(Collectors.toList()));
		List<GameItemRule> result = gameItemRuleMapper.selectList(ruleQueryWrapper);
		Map<Long, GameItemRule> ruleMap = result.stream()
			.collect(Collectors.toMap(GameItemRule::getItemId, Function.identity()));

		List<GameItemRuleVo> response = itemEntities.getRecords().stream().map(item -> {
			GameItemRuleVo ruleVo = new GameItemRuleVo();
			if (ruleMap.containsKey(item.getItemId())) {
				ruleVo.setRuleId(ruleMap.get(item.getItemId()).getRuleId());
				ruleVo.setRuleName(
						Objects.requireNonNull(SettingRuleEnum.getByCode(ruleMap.get(item.getItemId()).getRuleId()))
							.getValue());
			}
			ruleVo.setGameId(item.getGameId());
			ruleVo.setItemName(item.getItemName());
			ruleVo.setGroupName(item.getGroupName());
			ruleVo.setItemType(ItemTypeEnum.getNameByType(item.getItemType()));
			ruleVo.setItemId(item.getItemId());
			ruleVo.setGroupId(item.getGroupId());
			return ruleVo;
		}).collect(Collectors.toList());
		ruleResult.setRecords(response);
		ruleResult.setTotal(itemEntities.getTotal());
		return ApiResponse.success(ruleResult);
	}

	/**
	 * 设置项目规则
	 * @param query
	 * @return
	 */
	@Override
	public ApiResponse<Boolean> setItemRules(ItemRuleSetQuery query) {
		if (query.getSyncGroup()) {
			// 同组别同步设置规则
			GameItemListQuery itemListQuery = new GameItemListQuery();
			itemListQuery.setGameId(query.getGameId());
			itemListQuery.setGroupId(query.getGroupId());
			List<GameItemEntity> itemList = gameItemManager.fetchList(itemListQuery);
			for (GameItemEntity item : itemList) {
				GameItemRule gameItemRule = gameItemRuleManager.getGameItemRule(query.getGameId(), item.getId());
				if (Objects.nonNull(gameItemRule)) {
					gameItemRule.setRuleId(query.getRuleId());
				}
				else {
					gameItemRule = new GameItemRule();
					gameItemRule.setGameId(query.getGameId());
					gameItemRule.setItemId(item.getId());
					gameItemRule.setRuleId(query.getRuleId());
					gameItemRule.setCreatedAt(new Date());
				}
				gameItemRuleManager.saveItemRule(gameItemRule);
			}
		}
		else {
			GameItemRule gameItemRule = gameItemRuleManager.getGameItemRule(query.getGameId(), query.getItemId());
			if (Objects.nonNull(gameItemRule)) {
				gameItemRule.setRuleId(query.getRuleId());
			}
			else {
				gameItemRule = new GameItemRule();
				gameItemRule.setGameId(query.getGameId());
				gameItemRule.setItemId(query.getItemId());
				gameItemRule.setRuleId(query.getRuleId());
			}
			gameItemRuleManager.saveItemRule(gameItemRule);
		}

		return ApiResponse.success(true);
	}

	@Override
	public ApiResponse<GameItemRuleVo> getItemRulesDetail(ItemRuleQuery query) {
		return null;
	}

	@Override
	public ApiResponse<List<RuleVo>> getRules() {
		List<RuleVo> result = new ArrayList<>();
		for (SettingRuleEnum settingRuleEnum : SettingRuleEnum.values()) {
			RuleVo ruleVo = new RuleVo();
			ruleVo.setName(settingRuleEnum.name());
			ruleVo.setId(settingRuleEnum.getCode());
			result.add(ruleVo);
		}
		return ApiResponse.success(result);
	}

}

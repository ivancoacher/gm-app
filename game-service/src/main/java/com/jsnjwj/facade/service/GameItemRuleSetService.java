package com.jsnjwj.facade.service;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.query.rule.ItemRuleQuery;
import com.jsnjwj.facade.query.rule.ItemRuleSetQuery;
import com.jsnjwj.facade.vo.rule.GameItemRuleVo;
import com.jsnjwj.facade.vo.rule.RuleVo;

import java.util.List;

public interface GameItemRuleSetService {

    ApiResponse<List<GameItemRuleVo>> getItemRules(ItemRuleQuery query);

    ApiResponse<Boolean> setItemRules(ItemRuleSetQuery query);

    ApiResponse<GameItemRuleVo> getItemRulesDetail(ItemRuleQuery query);

    ApiResponse<List<RuleVo>> getRules();
}

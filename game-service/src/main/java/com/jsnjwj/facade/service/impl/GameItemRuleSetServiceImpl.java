package com.jsnjwj.facade.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.entity.GameItemEntity;
import com.jsnjwj.facade.entity.GameItemRule;
import com.jsnjwj.facade.enums.SettingRuleEnum;
import com.jsnjwj.facade.manager.GameItemManager;
import com.jsnjwj.facade.mapper.GameItemRuleMapper;
import com.jsnjwj.facade.query.GameItemListQuery;
import com.jsnjwj.facade.query.rule.ItemRuleQuery;
import com.jsnjwj.facade.query.rule.ItemRuleSetQuery;
import com.jsnjwj.facade.service.GameItemRuleSetService;
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

    /**
     * 获取所有项目对应的比赛规则
     * @param query
     * @return
     */
    @Override
    public ApiResponse<List<GameItemRuleVo>> getItemRules(ItemRuleQuery query) {

        GameItemListQuery gameItemListQuery = new GameItemListQuery();
        gameItemListQuery.setGameId(query.getGameId());
        gameItemListQuery.setGroupId(query.getGameId());

        List<GameItemEntity> itemEntities = gameItemManager.fetchList(gameItemListQuery);
        if (CollectionUtil.isEmpty(itemEntities)) {
            return ApiResponse.success(Collections.emptyList());
        }
        // 获取所有小项
        List<GameItemRuleVo> response = new ArrayList<>();
        LambdaQueryWrapper<GameItemRule> ruleQueryWrapper = new LambdaQueryWrapper<>();
        ruleQueryWrapper.eq(GameItemRule::getGameId, query.getGameId());
        ruleQueryWrapper.in(GameItemRule::getItemId,itemEntities.stream().map(GameItemEntity::getId).collect(Collectors.toList()));
        List<GameItemRule> result = gameItemRuleMapper.selectList(ruleQueryWrapper);
        Map<Long,GameItemRule> ruleMap = result.stream().collect(Collectors.toMap(GameItemRule::getItemId, Function.identity()));

        response = itemEntities.stream().map(item->{
            GameItemRuleVo ruleVo = new GameItemRuleVo();
            if (ruleMap.containsKey(item.getId())) {
                ruleVo.setRuleId(ruleMap.get(item.getId()).getRuleId());
                ruleVo.setRuleName(Objects.requireNonNull(SettingRuleEnum.getByCode(ruleMap.get(item.getId()).getRuleId())).getValue());
            }
            ruleVo.setGameId(item.getGameId());
            ruleVo.setItemName(item.getItemName());

            return ruleVo;
        }).collect(Collectors.toList());

        return ApiResponse.success(response);
    }

    @Override
    public ApiResponse<Boolean> setItemRules(ItemRuleSetQuery query) {
        return null;
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

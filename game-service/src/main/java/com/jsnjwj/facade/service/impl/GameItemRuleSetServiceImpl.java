package com.jsnjwj.facade.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.entity.GameItemEntity;
import com.jsnjwj.facade.entity.GameItemRule;
import com.jsnjwj.facade.enums.SettingRuleEnum;
import com.jsnjwj.facade.manager.GameItemManager;
import com.jsnjwj.facade.mapper.GameItemRuleMapper;
import com.jsnjwj.facade.query.rule.ItemRuleQuery;
import com.jsnjwj.facade.query.rule.ItemRuleSetQuery;
import com.jsnjwj.facade.service.GameItemRuleSetService;
import com.jsnjwj.facade.vo.rule.GameItemRuleVo;
import com.jsnjwj.facade.vo.rule.RuleVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameItemRuleSetServiceImpl implements GameItemRuleSetService {

    private final GameItemRuleMapper gameItemRuleMapper;
    private final GameItemManager gameItemManager;

    @Override
    public ApiResponse<List<GameItemRuleVo>> getItemRules(ItemRuleQuery query) {
        LambdaQueryWrapper<GameItemRule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GameItemRule::getGameId, query.getGameId());
        queryWrapper.eq(Objects.nonNull(query.getRuleId()),GameItemRule::getRuleId, query.getRuleId());

        List<GameItemRuleVo> response = new ArrayList<>();
        Set<Long> itemIds = result.stream().map(GameItemRule::getItemId).collect(Collectors.toSet());
        Map<Long, GameItemEntity> itemEntities = gameItemManager.fetchItemMap(CollectionUtil.newArrayList(itemIds));

        response = result.stream().map(item->{
            GameItemRuleVo ruleVo = new GameItemRuleVo();
            if (Objects.nonNull(item.getRuleId())){
                ruleVo.setRuleId(item.getRuleId());
                ruleVo.setRuleName(Objects.requireNonNull(SettingRuleEnum.getByCode(item.getRuleId())).getValue());
            }
            ruleVo.setGameId(item.getGameId());
            if (itemEntities.containsKey(item.getId())){
                ruleVo.setItemName(itemEntities.get(item.getId()).getItemName());
            }
            return ruleVo;
        }).collect(Collectors.toList());

        List<GameItemRule> result = gameItemRuleMapper.selectList(queryWrapper);
        if (CollectionUtil.isNotEmpty(result)){

        }

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

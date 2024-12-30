package com.jsnjwj.api.controller;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.common.utils.ThreadLocalUtil;
import com.jsnjwj.facade.query.rule.ItemRuleQuery;
import com.jsnjwj.facade.query.rule.ItemRuleSetQuery;
import com.jsnjwj.facade.service.GameArrangeService;
import com.jsnjwj.facade.service.GameItemRuleSetService;
import com.jsnjwj.facade.service.GameSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/game/setting/rule")
public class GameSettingRuleController {

    private final GameItemRuleSetService gameItemRuleSetService;

    /**
     * 获取项目规则列表
     *
     * @param query
     * @return
     */
    @GetMapping("/getItemRuleList")
    public ApiResponse<?> getItemRuleList() {
        ItemRuleQuery query = new ItemRuleQuery();
        query.setGameId(ThreadLocalUtil.getCurrentGameId());
        query.setUserId(ThreadLocalUtil.getCurrentUserId());
        return gameItemRuleSetService.getItemRules(query);
    }

    /**
     * 设置项目规则
     *
     * @param query
     * @return
     */
    @GetMapping("/setItemRule")
    public ApiResponse<?> setItemRule(@RequestBody ItemRuleSetQuery query) {
        query.setGameId(ThreadLocalUtil.getCurrentGameId());
        query.setUserId(ThreadLocalUtil.getCurrentUserId());
        return gameItemRuleSetService.setItemRules(query);
    }

    /**
     * 获取项目规则详情
     *
     * @param query
     * @return
     */
    @GetMapping("/getItemRuleDetail")
    public ApiResponse<?> getItemRuleDetail(@RequestBody ItemRuleQuery query) {
        query.setGameId(ThreadLocalUtil.getCurrentGameId());
        query.setUserId(ThreadLocalUtil.getCurrentUserId());
        return gameItemRuleSetService.getItemRulesDetail(query);
    }

    /**
     * 获取规则
     * @return
     */
    @PostMapping("/getRuleList")
    public ApiResponse<?> getRule() {
        return gameItemRuleSetService.getRules();
    }

}

package com.jsnjwj.api.controller;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.common.utils.ThreadLocalUtil;
import com.jsnjwj.facade.query.GameSettingSetRulesQuery;
import com.jsnjwj.facade.service.GameSettingService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/game/setting/rule")
public class GameSettingRuleController {

    @Resource
    private GameSettingService gameSettingService;

    @GetMapping("/setRule")
    public ApiResponse<?> getAreas(@RequestBody GameSettingSetRulesQuery query) {
        query.setGameId(ThreadLocalUtil.getCurrentGameId());
        query.setUserId(ThreadLocalUtil.getCurrentUserId());
        return gameSettingService.setRules(query);
    }

    @PostMapping("/getRule")
    public ApiResponse<?> setAreaNum(@RequestParam("gameId") Long gameId, @RequestParam("itemId") Long itemId) {
        return gameSettingService.getRules(gameId, itemId);
    }

}

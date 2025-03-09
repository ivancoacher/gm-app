package com.jsnjwj.api.controller;

import com.jsnjwj.facade.service.GameSettingService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 赛事设置
 */
@RestController
@RequestMapping("/game/setting")
public class GameSettingController {

    @Resource
    private GameSettingService gameSettingService;


}

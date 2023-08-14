package com.jsnjwj.api.controller;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.query.GameGroupingListQuery;
import com.jsnjwj.facade.query.GameGroupingSetQuery;
import com.jsnjwj.facade.service.GameSettingService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 项目分组
 */
@RestController
@RequestMapping("/game/setting/arrange")
public class GameSettingArrangeController {

    @Resource
    private GameSettingService gameSettingService;

}

package com.jsnjwj.api.controller;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.entity.GameAreaEntity;
import com.jsnjwj.facade.query.GameGroupingAreaSetQuery;
import com.jsnjwj.facade.query.GameGroupingSetNumQuery;
import com.jsnjwj.facade.service.GameSettingService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/game/setting")
public class GameSettingController {

    @Resource
    private GameSettingService gameSettingService;

    @GetMapping("/getAreas")
    public ApiResponse<List<GameAreaEntity>> getAreas(@RequestParam("gameId") Long gameId) {
        return gameSettingService.getCourts(gameId);
    }

    @PostMapping("/setAreaNum")
    public ApiResponse<?> setAreaNum(@RequestBody GameGroupingSetNumQuery query) {
        return gameSettingService.setCourtNum(query);
    }

    @PostMapping("/saveArea")
    public ApiResponse<Boolean> saveArea(@RequestBody GameGroupingAreaSetQuery query) {
        return gameSettingService.saveCourt(query);
    }

}

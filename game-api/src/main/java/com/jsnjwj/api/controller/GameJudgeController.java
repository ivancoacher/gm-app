package com.jsnjwj.api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.query.GameJudgeAssignQuery;
import com.jsnjwj.facade.query.GameJudgeListQuery;
import com.jsnjwj.facade.service.GameJudgeService;
import com.jsnjwj.facade.vo.GroupLabelVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/game/setting/judge")
public class GameJudgeController {

    @Resource
    private GameJudgeService gameJudgeService;

    @RequestMapping("/list")
    public ApiResponse<Page<GroupLabelVo>> fetchPage(GameJudgeListQuery query) {
        return ApiResponse.success(gameJudgeService.fetchPage(query));
    }

    @RequestMapping("/data")
    public ApiResponse<Page<GroupLabelVo>> fetchData(GameJudgeListQuery query) {
        return ApiResponse.success(gameJudgeService.fetchList(query.getGameId()));
    }

    @RequestMapping("/assign")
    public ApiResponse<Boolean> assign(GameJudgeAssignQuery query) {
        return ApiResponse.success(gameJudgeService.assign(query));
    }

}

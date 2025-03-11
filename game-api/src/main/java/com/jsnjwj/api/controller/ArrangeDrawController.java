package com.jsnjwj.api.controller;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.common.utils.ThreadLocalUtil;
import com.jsnjwj.facade.entity.GameSessionEntity;
import com.jsnjwj.facade.query.session.*;
import com.jsnjwj.facade.service.v2.ArrangeSessionItemService;
import com.jsnjwj.facade.service.v2.ArrangeSessionService;
import com.jsnjwj.facade.service.v2.DrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 抽签分组
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/game/setting/arrange/draw")
public class ArrangeDrawController {

    private final DrawService drawService;


    /**
     * 场地列表
     *
     * @return
     */
    @PostMapping("/system")
    public ApiResponse<?> systemDraw(@RequestBody SystemDrawQuery query) {
        Long gameId = ThreadLocalUtil.getCurrentGameId();
        query.setGameId(gameId);
        return drawService.systemDraw(query);
    }

    @PostMapping("/get")
    public ApiResponse<?> getDraw(@RequestBody SystemDrawQuery query) {
        Long gameId = ThreadLocalUtil.getCurrentGameId();
        query.setGameId(gameId);
        return drawService.getDraw(query);
    }

    @PostMapping("/manual")
    public ApiResponse<?> manualDraw(@RequestBody ManualDrawQuery query) {
        query.setGameId(ThreadLocalUtil.getCurrentGameId());
        return drawService.manualDraw(query);
    }

}

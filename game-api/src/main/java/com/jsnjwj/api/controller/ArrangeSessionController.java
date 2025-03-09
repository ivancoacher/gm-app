package com.jsnjwj.api.controller;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.common.utils.ThreadLocalUtil;
import com.jsnjwj.facade.entity.GameSessionEntity;
import com.jsnjwj.facade.query.GameGroupingSessionSetNumQuery;
import com.jsnjwj.facade.query.GameGroupingSessionSetQuery;
import com.jsnjwj.facade.service.v2.ArrangeSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 项目分组
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/game/setting/arrange/session")
public class ArrangeSessionController {

    private final ArrangeSessionService arrangeSessionService;

    /**
     * 场地列表
     *
     * @return
     */
    @GetMapping("/list")
    public ApiResponse<List<GameSessionEntity>> getSessions() {
        Long gameId = ThreadLocalUtil.getCurrentGameId();
        return arrangeSessionService.getSessions(gameId);
    }

    /**
     * 设置场地数量
     *
     * @param query
     * @return
     */
    @PostMapping("/setNum")
    public ApiResponse<?> setSessionNum(@RequestBody GameGroupingSessionSetNumQuery query) {
        query.setGameId(ThreadLocalUtil.getCurrentGameId());
        return arrangeSessionService.setSessionNum(query);
    }

    /**
     * 修改场地信息
     *
     * @param query
     */
    @PostMapping("/update")
    public ApiResponse<Boolean> saveSession(@RequestBody GameGroupingSessionSetQuery query) {
        query.setGameId(ThreadLocalUtil.getCurrentGameId());
        return arrangeSessionService.saveSession(query);
    }

}

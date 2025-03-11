package com.jsnjwj.api.controller;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.common.utils.ThreadLocalUtil;
import com.jsnjwj.facade.entity.GameSessionEntity;
import com.jsnjwj.facade.query.session.GameGroupingSessionSetNumQuery;
import com.jsnjwj.facade.query.session.GameGroupingSessionSetQuery;
import com.jsnjwj.facade.query.session.SessionItemGetQuery;
import com.jsnjwj.facade.query.session.SessionItemSetQuery;
import com.jsnjwj.facade.service.v2.ArrangeSessionItemService;
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

    private final ArrangeSessionItemService arrangeSessionItemService;

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

    @GetMapping("/item/unSelected")
    public ApiResponse<?> getUnSelected() {
        SessionItemGetQuery query = new SessionItemGetQuery();
        query.setGameId(ThreadLocalUtil.getCurrentGameId());
        return arrangeSessionItemService.getUnSelectedItem(query);
    }

    @GetMapping("/item/selected")
    public ApiResponse<?> getSelected(@RequestParam("sessionId") Long sessionId) {
        SessionItemGetQuery query = new SessionItemGetQuery();
        query.setSessionId(sessionId);
        query.setGameId(ThreadLocalUtil.getCurrentGameId());
        return arrangeSessionItemService.getSelectedItem(query);
    }

    @PostMapping("/item/save")
    public ApiResponse<Boolean> saveItem(@RequestBody SessionItemSetQuery query) {
        query.setGameId(ThreadLocalUtil.getCurrentGameId());
        return arrangeSessionItemService.saveSessionItem(query);
    }

}

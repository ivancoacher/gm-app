package com.jsnjwj.api.controller;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.common.utils.ThreadLocalUtil;
import com.jsnjwj.facade.entity.GameAreaEntity;
import com.jsnjwj.facade.query.GameGroupingAreaSetQuery;
import com.jsnjwj.facade.query.GameGroupingSetNumQuery;
import com.jsnjwj.facade.service.GameArrangeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 场次管理
 */
@RestController
@RequestMapping("/game/setting/arrange/session")
public class GameSettingSessionController {

    @Resource
    private GameArrangeService gameArrangeService;

    /**
     * 场地列表
     *
     * @return
     */
    @GetMapping("/list")
    public ApiResponse<List<GameAreaEntity>> getAreas() {

        Long gameId = ThreadLocalUtil.getCurrentGameId();
        return gameArrangeService.getCourts(gameId);
    }

    /**
     * 设置场地数量
     *
     * @param query
     * @return
     */
    @PostMapping("/setNum")
    public ApiResponse<?> setAreaNum(@RequestBody GameGroupingSetNumQuery query) {
        query.setGameId(ThreadLocalUtil.getCurrentGameId());
        return gameArrangeService.setCourtNum(query);
    }

    /**
     * 修改场地信息
     *
     * @param query
     * @return
     */
    @PostMapping("/update")
    public ApiResponse<Boolean> saveArea(@RequestBody GameGroupingAreaSetQuery query) {
        query.setGameId(ThreadLocalUtil.getCurrentGameId());
        return gameArrangeService.saveCourt(query);
    }

}

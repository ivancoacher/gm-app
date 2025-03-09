package com.jsnjwj.api.controller;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.common.utils.ThreadLocalUtil;
import com.jsnjwj.facade.entity.GameAreaEntity;
import com.jsnjwj.facade.query.GameGroupingAreaSetQuery;
import com.jsnjwj.facade.query.GameGroupingSetNumQuery;
import com.jsnjwj.facade.service.v2.ArrangeAreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 项目分组
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/game/setting/arrange/area")
public class ArrangeAreaController {

    private final ArrangeAreaService arrangeAreaService;

    /**
     * 场地列表
     *
     * @return
     */
    @GetMapping("/list")
    public ApiResponse<List<GameAreaEntity>> getAreas() {

        Long gameId = ThreadLocalUtil.getCurrentGameId();
        return arrangeAreaService.getAreas(gameId);
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
        return arrangeAreaService.setAreaNum(query);
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
        return arrangeAreaService.saveArea(query);
    }

}

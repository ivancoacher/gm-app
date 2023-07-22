package com.jsnjwj.api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.query.GameItemListQuery;
import com.jsnjwj.facade.query.GameItemSaveQuery;
import com.jsnjwj.facade.query.GameItemUpdateQuery;
import com.jsnjwj.facade.service.GameItemService;
import com.jsnjwj.facade.vo.GroupLabelVo;
import com.jsnjwj.facade.vo.ItemLabelVo;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/game/item")
public class GameItemController {

    @Resource
    private GameItemService gameItemService;

    @RequestMapping("/list")
    public ApiResponse<Page<ItemLabelVo>> fetchPage(GameItemListQuery query) {
        return gameItemService.fetchPages(query);
    }

    @RequestMapping("/data")
    public ApiResponse<List<GroupLabelVo>> fetchList(GameItemListQuery query) {
        return ApiResponse.success(gameItemService.fetchList(query));
    }

    @RequestMapping("/save")
    public ApiResponse<List<GroupLabelVo>> save(@RequestBody GameItemSaveQuery query) {
        return ApiResponse.success(gameItemService.save(query));
    }

    @RequestMapping("/update")
    public ApiResponse<List<GroupLabelVo>> update(@RequestBody GameItemUpdateQuery query) {
        return ApiResponse.success(gameItemService.update(query));
    }

    @DeleteMapping("/delete")
    public ApiResponse<?> delete(GameItemUpdateQuery query) {
        return gameItemService.delete(query);
    }
}

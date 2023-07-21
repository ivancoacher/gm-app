package com.jsnjwj.api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.query.GameGroupListQuery;
import com.jsnjwj.facade.query.GameItemListQuery;
import com.jsnjwj.facade.service.GameGroupService;
import com.jsnjwj.facade.service.GameItemService;
import com.jsnjwj.facade.vo.GameItemVo;
import com.jsnjwj.facade.vo.GroupLabelVo;
import com.jsnjwj.facade.vo.ItemLabelVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/game/item")
public class GameItemController {

	@Resource
	private GameItemService gameItemService;

	@RequestMapping("/list")
	public ApiResponse<Page<ItemLabelVo>> fetchList(GameItemListQuery query) {
		return gameItemService.fetchPages(query);
	}

}

package com.jsnjwj.api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.request.BaseRequest;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.common.utils.ThreadLocalUtil;
import com.jsnjwj.facade.query.GameItemListQuery;
import com.jsnjwj.facade.query.GameItemSaveQuery;
import com.jsnjwj.facade.query.GameItemUpdateQuery;
import com.jsnjwj.facade.service.GameItemService;
import com.jsnjwj.facade.vo.GroupLabelVo;
import com.jsnjwj.facade.vo.ItemLabelVo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;
import java.util.List;

/**
 * 赛事项目设置
 */
@RestController
@RequestMapping("/game/item")
public class GameItemController {

	@Resource
	private GameItemService gameItemService;

	@RequestMapping("/list")
	public ApiResponse<Page<ItemLabelVo>> fetchPage(GameItemListQuery query) {
		query.setGameId(ThreadLocalUtil.getCurrentGameId());
		return gameItemService.fetchPages(query);
	}

	@RequestMapping("/data")
	public ApiResponse<List<GroupLabelVo>> fetchList(GameItemListQuery query) {
		query.setGameId(ThreadLocalUtil.getCurrentGameId());
		return ApiResponse.success(gameItemService.fetchList(query));
	}

	@GetMapping("/info/{id}")
	public ApiResponse<List<GroupLabelVo>> fetchOne(@PathVariable("id") Long itemId) {

		return ApiResponse.success(gameItemService.fetchOne(itemId));
	}

	@RequestMapping("/save")
	public ApiResponse<List<GroupLabelVo>> save(@RequestBody GameItemSaveQuery query) {
		return ApiResponse.success(gameItemService.save(query));
	}

	@RequestMapping("/update")
	public ApiResponse update(@RequestBody GameItemUpdateQuery query) {
		return ApiResponse.success(gameItemService.update(query));
	}

	@DeleteMapping("/delete")
	public ApiResponse<?> delete(GameItemUpdateQuery query) {
		return gameItemService.delete(query);
	}

}

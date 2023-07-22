package com.jsnjwj.api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.query.GameGroupListQuery;
import com.jsnjwj.facade.query.GameGroupSaveQuery;
import com.jsnjwj.facade.query.GameGroupUpdateQuery;
import com.jsnjwj.facade.service.GameGroupService;
import com.jsnjwj.facade.vo.GameGroupVo;
import com.jsnjwj.facade.vo.GroupLabelVo;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/game/group")
public class GameGroupController {

	@Resource
	private GameGroupService gameGroupService;

	@RequestMapping("/list")
	public ApiResponse<Page<GroupLabelVo>> fetchPage(GameGroupListQuery query) {
		return ApiResponse.success(gameGroupService.fetchPages(query));
	}

	@RequestMapping("/data")
	public ApiResponse<List<GroupLabelVo>> fetchList(GameGroupListQuery query) {
		return ApiResponse.success(gameGroupService.fetchList(query));
	}

	@RequestMapping("/save")
	public ApiResponse<List<GroupLabelVo>> save(@RequestBody GameGroupSaveQuery query) {
		return ApiResponse.success(gameGroupService.save(query));
	}

	@RequestMapping("/update")
	public ApiResponse<List<GroupLabelVo>> update(@RequestBody GameGroupUpdateQuery query) {
		return ApiResponse.success(gameGroupService.update(query));
	}

	@DeleteMapping("/delete")
	public ApiResponse<?> delete(GameGroupUpdateQuery query) {
		return gameGroupService.delete(query);
	}
}

package com.jsnjwj.api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.request.BaseRequest;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.common.utils.ThreadLocalUtil;
import com.jsnjwj.facade.query.GameGroupListQuery;
import com.jsnjwj.facade.query.GameGroupSaveQuery;
import com.jsnjwj.facade.query.GameGroupUpdateQuery;
import com.jsnjwj.facade.service.GameGroupService;
import com.jsnjwj.facade.vo.GroupLabelVo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * 组别设置
 */
@RestController
@RequestMapping("/game/group")
public class GameGroupController {

	@Resource
	private GameGroupService gameGroupService;

	@RequestMapping("/list")
	public ApiResponse<Page<GroupLabelVo>> fetchPage(GameGroupListQuery query) {
		query.setGameId(ThreadLocalUtil.getCurrentGameId());
		return ApiResponse.success(gameGroupService.fetchPages(query));
	}

	@RequestMapping("/data")
	public ApiResponse<List<GroupLabelVo>> fetchList(GameGroupListQuery query) {
		query.setGameId(ThreadLocalUtil.getCurrentGameId());
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

	@DeleteMapping("/{groupId}")
	public ApiResponse<?> delete(@PathVariable Long groupId) {
		return gameGroupService.delete(groupId);
	}

}

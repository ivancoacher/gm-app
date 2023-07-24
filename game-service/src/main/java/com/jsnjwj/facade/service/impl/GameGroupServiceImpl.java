package com.jsnjwj.facade.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.entity.TcGameGroup;
import com.jsnjwj.facade.manager.GameGroupManager;
import com.jsnjwj.facade.query.GameGroupListQuery;
import com.jsnjwj.facade.query.GameGroupSaveQuery;
import com.jsnjwj.facade.query.GameGroupUpdateQuery;
import com.jsnjwj.facade.service.GameGroupService;
import com.jsnjwj.facade.vo.GameGroupAllVo;

import com.jsnjwj.facade.vo.GroupLabelVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GameGroupServiceImpl implements GameGroupService {

	@Resource
	private GameGroupManager groupManager;

	/**
	 * 分页查询
	 * @return
	 */
	@Override
	public Page<GroupLabelVo> fetchPages(GameGroupListQuery query) {

		return groupManager.fetchGroupPage(query.getGameId());
	}

	/**
	 * 查询全部
	 * @return
	 */
	@Override
	public List<GroupLabelVo> fetchList(GameGroupListQuery query) {
		return groupManager.fetchGroups(query.getGameId());
	}

	/**
	 * 查询包含项目数据
	 * @return
	 */
	@Override
	public List<GameGroupAllVo> fetchAll(Long gameId) {
		return null;
	}

	@Override
	public ApiResponse<?> save(GameGroupSaveQuery query) {
		TcGameGroup gameGroup = new TcGameGroup();
		gameGroup.setGameId(query.getGameId());
		gameGroup.setGroupName(query.getGroupName());
		gameGroup.setSort(query.getSort());

		int result = groupManager.save(gameGroup);
		return ApiResponse.success(result);
	}

	@Override
	public ApiResponse<?> update(GameGroupUpdateQuery query) {
		TcGameGroup gameGroup = new TcGameGroup();
		gameGroup.setGameId(query.getGameId());
		gameGroup.setGroupName(query.getGroupName());
		gameGroup.setSort(query.getSort());
		gameGroup.setId(query.getGroupId());
		int result = groupManager.update(gameGroup);
		return ApiResponse.success(result);
	}

	@Override
	public ApiResponse<?> delete(GameGroupUpdateQuery query) {
		int result = groupManager.deleteGroup(query.getGroupId());
		return ApiResponse.success(result);
	}

}

package com.jsnjwj.facade.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.request.BaseRequest;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.common.utils.ThreadLocalUtil;
import com.jsnjwj.facade.entity.GameGroupEntity;
import com.jsnjwj.facade.entity.SignSingleEntity;
import com.jsnjwj.facade.manager.GameGroupManager;
import com.jsnjwj.facade.manager.SignApplyManager;
import com.jsnjwj.facade.query.GameGroupBatchUpdateQuery;
import com.jsnjwj.facade.query.GameGroupListQuery;
import com.jsnjwj.facade.query.GameGroupSaveQuery;
import com.jsnjwj.facade.query.GameGroupUpdateQuery;
import com.jsnjwj.facade.service.GameGroupService;
import com.jsnjwj.facade.vo.GameGroupAllVo;
import com.jsnjwj.facade.vo.GroupLabelVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameGroupServiceImpl implements GameGroupService {

	private final GameGroupManager groupManager;

	private final SignApplyManager signApplyManager;

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
	 * 查询包含项目数据 group+item
	 * @return List<GameGroupAllVo>
	 */
	@Override
	public List<GameGroupAllVo> fetchAll(Long gameId) {
		return null;
	}

	@Override
	public ApiResponse<?> save(GameGroupSaveQuery query) {
		GameGroupEntity gameGroup = new GameGroupEntity();
		gameGroup.setGameId(query.getGameId());
		gameGroup.setGroupName(query.getGroupName());
		gameGroup.setSort(query.getSort());

		int result = groupManager.save(gameGroup);
		return ApiResponse.success(result);
	}

	@Override
	public ApiResponse<?> importData(BaseRequest request, MultipartFile file) {
		GameGroupEntity gameGroup = new GameGroupEntity();
		gameGroup.setGameId(request.getGameId());
		return ApiResponse.success(true);
	}

	@Override
	public ApiResponse<?> update(GameGroupUpdateQuery query) {
		GameGroupEntity gameGroup = new GameGroupEntity();
		gameGroup.setGameId(query.getGameId());
		gameGroup.setGroupName(query.getGroupName());
		gameGroup.setSort(query.getSort());
		gameGroup.setId(query.getGroupId());
		int result = groupManager.update(gameGroup);
		return ApiResponse.success(result);
	}

	@Override
	public ApiResponse<?> updateBatch(GameGroupBatchUpdateQuery request) {
		for (GroupLabelVo query : request.getData()) {
			GameGroupEntity tcGameGroup = new GameGroupEntity();
			tcGameGroup.setId(query.getGroupId());
			tcGameGroup.setSort(query.getSort());
			tcGameGroup.setGroupName(query.getGroupName());
			groupManager.update(tcGameGroup);
		}
		return ApiResponse.success(true);
	}

	@Override
	public ApiResponse<?> delete(Long groupId) {
		Long gameId = ThreadLocalUtil.getCurrentGameId();
		// 判断该组别下是否还有报名信息
		List<SignSingleEntity> singleEntities = signApplyManager.getSignByGroupId(gameId, groupId);
		ApiResponse<Boolean> response = new ApiResponse<>();
		if (CollUtil.isNotEmpty(singleEntities)) {
			response.setData(false);
			response.setMessage("请先删除该组别下所有报名信息");
			return response;
		}

		int result = groupManager.deleteGroup(groupId);
		return ApiResponse.success(result > 0);
	}

}

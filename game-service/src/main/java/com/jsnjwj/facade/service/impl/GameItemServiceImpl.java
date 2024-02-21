package com.jsnjwj.facade.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.request.BaseRequest;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.common.utils.ThreadLocalUtil;
import com.jsnjwj.facade.entity.GameItemEntity;
import com.jsnjwj.facade.entity.SignSingleEntity;
import com.jsnjwj.facade.manager.GameItemManager;
import com.jsnjwj.facade.manager.SignApplyManager;
import com.jsnjwj.facade.query.GameItemBatchUpdateQuery;
import com.jsnjwj.facade.query.GameItemListQuery;
import com.jsnjwj.facade.query.GameItemSaveQuery;
import com.jsnjwj.facade.query.GameItemUpdateQuery;
import com.jsnjwj.facade.service.GameItemService;
import com.jsnjwj.facade.vo.ItemLabelVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameItemServiceImpl implements GameItemService {

	private final GameItemManager gameItemManager;

	private final SignApplyManager signApplyManager;

	/**
	 * 分页查询
	 * @return
	 */
	@Override
	public ApiResponse<Page<ItemLabelVo>> fetchPages(GameItemListQuery query) {

		return gameItemManager.fetchItemsPage(query);
	}

	/**
	 * 查询全部
	 * @return
	 */
	@Override
	public List<ItemLabelVo> fetchList(GameItemListQuery query) {
		List<GameItemEntity> result = gameItemManager.fetchList(query);

		List<ItemLabelVo> response = new ArrayList<>();

		result.forEach(item -> {
			ItemLabelVo vo = new ItemLabelVo();
			vo.setItemName(item.getItemName());
			vo.setItemId(item.getId());
			response.add(vo);
		});
		return response;

	}

	@Override
	public void importData(BaseRequest query, MultipartFile file) {

	}

	@Override
	public Boolean save(GameItemSaveQuery query) {

		if (CollUtil.isNotEmpty(query.getGroupId())) {
			for (Long groupId : query.getGroupId()) {
				GameItemEntity tcGameGroup = new GameItemEntity();
				tcGameGroup.setGameId(query.getGameId());
				tcGameGroup.setGroupId(groupId);
				tcGameGroup.setItemName(query.getItemName());
				tcGameGroup.setItemType(query.getItemType());
				tcGameGroup.setSort(query.getSort());
				gameItemManager.save(tcGameGroup);
			}
			return true;
		}
		return false;

	}

	@Override
	public int update(GameItemUpdateQuery query) {
		GameItemEntity tcGameGroup = new GameItemEntity();
		tcGameGroup.setGroupId(query.getGroupId());
		tcGameGroup.setSort(query.getSort());
		tcGameGroup.setId(query.getId());
		tcGameGroup.setItemName(query.getItemName());
		tcGameGroup.setItemType(query.getItemType());
		return gameItemManager.update(tcGameGroup);
	}

	@Override
	public boolean updateBatch(GameItemBatchUpdateQuery request) {
		for (ItemLabelVo query : request.getData()) {
			GameItemEntity tcGameGroup = new GameItemEntity();
			tcGameGroup.setGroupId(query.getGroupId());
			tcGameGroup.setSort(query.getSort());
			tcGameGroup.setId(query.getItemId());
			tcGameGroup.setItemName(query.getItemName());
			tcGameGroup.setItemType(query.getItemType());
			gameItemManager.update(tcGameGroup);

		}
		return true;
	}

	@Override
	public GameItemEntity fetchOne(Long itemId) {

		return gameItemManager.fetchItemInfo(itemId);
	}

	@Override
	public ApiResponse<?> delete(Long itemId) {

		Long gameId = ThreadLocalUtil.getCurrentGameId();
		// 判断该组别下是否还有报名信息
		List<SignSingleEntity> singleEntities = signApplyManager.getSingByItemId(gameId, itemId);
		ApiResponse<Boolean> response = new ApiResponse<>();
		if (CollUtil.isNotEmpty(singleEntities)) {
			response.setData(false);
			response.setMessage("请先删除该项目下所有报名信息");
			return response;
		}
		int result = gameItemManager.delete(itemId);
		return ApiResponse.success(result > 0);
	}

}

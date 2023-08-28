package com.jsnjwj.facade.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.entity.GameItemEntity;
import com.jsnjwj.facade.manager.GameItemManager;
import com.jsnjwj.facade.query.GameItemListQuery;
import com.jsnjwj.facade.query.GameItemSaveQuery;
import com.jsnjwj.facade.query.GameItemUpdateQuery;
import com.jsnjwj.facade.service.GameItemService;
import com.jsnjwj.facade.vo.ItemLabelVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class GameItemServiceImpl implements GameItemService {

	@Resource
	private GameItemManager gameItemManager;

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
	public void importData() {

	}

	@Override
	public int save(GameItemSaveQuery query) {
		GameItemEntity tcGameGroup = new GameItemEntity();
		tcGameGroup.setGameId(query.getGameId());
		tcGameGroup.setGroupId(query.getGroupId());
		tcGameGroup.setItemName(query.getItemName());
		tcGameGroup.setSort(query.getSort());
		return gameItemManager.save(tcGameGroup);
	}

	@Override
	public int update(GameItemUpdateQuery query) {
		GameItemEntity tcGameGroup = new GameItemEntity();
		tcGameGroup.setGroupId(query.getGroupId());
		tcGameGroup.setSort(query.getSort());
		tcGameGroup.setId(query.getItemId());
		tcGameGroup.setItemName(query.getItemName());
		return gameItemManager.update(tcGameGroup);
	}

	@Override
	public GameItemEntity fetchOne(Long itemId) {

		return gameItemManager.fetchItemInfo(itemId);
	}

	@Override
	public ApiResponse<Integer> delete(GameItemUpdateQuery query) {
		return ApiResponse.success(gameItemManager.delete(query.getItemId()));
	}

}

package com.jsnjwj.facade.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.entity.TcGameItem;
import com.jsnjwj.facade.mapper.TcGameItemMapper;
import com.jsnjwj.facade.query.GameItemListQuery;
import com.jsnjwj.facade.vo.ItemLabelVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameItemManager {

	private final TcGameItemMapper gameItemMapper;

	public List<ItemLabelVo> fetchItems(Long gameId) {
		List<ItemLabelVo> response = new ArrayList<>();
		return response;
	}

	public ApiResponse<Page<ItemLabelVo>> fetchItemsPage(GameItemListQuery query) {
		Long gameId = query.getGameId();
		Page<TcGameItem> page = new Page<>();
		LambdaQueryWrapper<TcGameItem> lambdaQuery = new LambdaQueryWrapper<>();
		lambdaQuery.eq(TcGameItem::getGameId, gameId);

		lambdaQuery.eq(!StringUtils.isEmpty(query.getGroupId()), TcGameItem::getGroupId, query.getGroupId());
		Page<ItemLabelVo> result = gameItemMapper.selectByPage(page, lambdaQuery);

		return ApiResponse.success(result);
	}

	public List<TcGameItem> fetchList(GameItemListQuery query) {
		LambdaQueryWrapper<TcGameItem> lambdaQuery = new LambdaQueryWrapper<>();
		lambdaQuery.eq(TcGameItem::getGameId, query.getGameId());
		lambdaQuery.eq(TcGameItem::getGroupId, query.getGroupId());

		return gameItemMapper.selectList(lambdaQuery);
	}

	public List<ItemLabelVo> fetchItemsByGroupId(Long groupId) {
		List<ItemLabelVo> response = new ArrayList<>();
		return response;
	}

	public int save(TcGameItem tcGameItem) {
		return gameItemMapper.insert(tcGameItem);
	}

	public int update(TcGameItem tcGameItem) {
		return gameItemMapper.updateById(tcGameItem);
	}

	public int delete(Long itemId) {
		return gameItemMapper.deleteById(itemId);
	}

	public TcGameItem fetchItemInfo(Long itemId) {
		return gameItemMapper.selectById(itemId);
	}

}
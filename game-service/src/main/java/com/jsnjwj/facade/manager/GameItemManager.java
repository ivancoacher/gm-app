package com.jsnjwj.facade.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.entity.GameItemEntity;
import com.jsnjwj.facade.mapper.GameItemMapper;
import com.jsnjwj.facade.query.GameItemListQuery;
import com.jsnjwj.facade.vo.ItemLabelVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameItemManager {

	private final GameItemMapper gameItemMapper;

	public List<ItemLabelVo> fetchItems(Long gameId) {
		List<ItemLabelVo> response = new ArrayList<>();
		return response;
	}

	public ApiResponse<Page<ItemLabelVo>> fetchItemsPage(GameItemListQuery query) {
		Long gameId = query.getGameId();
		Page<GameItemEntity> page = new Page<>();
		LambdaQueryWrapper<GameItemEntity> lambdaQuery = new LambdaQueryWrapper<>();
		lambdaQuery.eq(GameItemEntity::getGameId, gameId);

		lambdaQuery.eq(!StringUtils.isEmpty(query.getGroupId()), GameItemEntity::getGroupId, query.getGroupId());
		Page<ItemLabelVo> result = gameItemMapper.selectByPage(page, lambdaQuery);

		return ApiResponse.success(result);
	}

	public List<GameItemEntity> fetchList(GameItemListQuery query) {
		LambdaQueryWrapper<GameItemEntity> lambdaQuery = new LambdaQueryWrapper<>();
		lambdaQuery.eq(GameItemEntity::getGameId, query.getGameId());
		lambdaQuery.eq(GameItemEntity::getGroupId, query.getGroupId());

		return gameItemMapper.selectList(lambdaQuery);
	}

	public List<ItemLabelVo> fetchItemsByGroupId(Long groupId) {
		List<ItemLabelVo> response = new ArrayList<>();
		return response;
	}

	public int save(GameItemEntity gameItemEntity) {
		return gameItemMapper.insert(gameItemEntity);
	}

	public int update(GameItemEntity gameItemEntity) {
		return gameItemMapper.updateById(gameItemEntity);
	}

	public int delete(Long itemId) {
		return gameItemMapper.deleteById(itemId);
	}

	public GameItemEntity fetchItemInfo(Long itemId) {
		return gameItemMapper.selectById(itemId);
	}

}

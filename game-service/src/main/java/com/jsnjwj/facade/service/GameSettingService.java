package com.jsnjwj.facade.service;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.entity.TcGameArea;
import com.jsnjwj.facade.query.*;

import java.util.List;

public interface GameSettingService {

	ApiResponse<?> setCourtNum(GameGroupingSetNumQuery query);

	ApiResponse<Boolean> saveCourt(GameGroupingAreaSetQuery query);

	ApiResponse<List<TcGameArea>> getCourts(Long gameId);

	ApiResponse<Boolean> setGroupingBatch(GameGroupingSetQuery query);

	ApiResponse<Boolean> setGrouping(GameGroupingSetQuery query);

	/**
	 * 场地编排
	 * @param query
	 * @return
	 */
	ApiResponse<?> fetchArrangeList(GameGroupingListQuery query);

	ApiResponse<?> setRules(GameSettingSetRulesQuery query);

	ApiResponse<?> getRules(Long gameId, Long itemId);

	/**
	 * 获取全部场地分组
	 * @param gameId
	 * @return
	 */
	// ApiResponse<?> getCourtItems(Long gameId);

	/**
	 * 获取单个场地分组
	 * @param gameId
	 * @param itemId
	 * @return
	 */
	ApiResponse<?> getCourtItem(Long gameId, Long itemId);

	/**
	 * 批量设置场地分组
	 * @return
	 */
	ApiResponse<?> batchGroupingItemCourt(Long gameId);

}

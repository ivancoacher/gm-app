package com.jsnjwj.facade.service;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.entity.TcGameArea;
import com.jsnjwj.facade.query.GameGroupingSetNumQuery;
import com.jsnjwj.facade.query.GameGroupingAreaSetQuery;
import com.jsnjwj.facade.query.GameGroupingSetQuery;
import com.jsnjwj.facade.query.GameSettingSetRulesQuery;

import java.util.List;

public interface GameSettingService {

	ApiResponse<?> setCourtNum(GameGroupingSetNumQuery query);

	ApiResponse<Boolean> saveCourt(GameGroupingAreaSetQuery query);

	ApiResponse<List<TcGameArea>> getCourts(Long gameId);

	ApiResponse<Boolean> setGrouping(GameGroupingSetQuery query);

	ApiResponse<?> setRules(GameSettingSetRulesQuery query);

	ApiResponse<?> getRules(Long gameId, Long itemId);

	/**
	 * 获取全部场地分组
	 * @param gameId
	 * @return
	 */
	ApiResponse<?> getCourtItems(Long gameId);

	/**
	 * 获取单个场地分组
	 * @param gameId
	 * @param itemId
	 * @return
	 */
	ApiResponse<?> getCourtItem(Long gameId,Long itemId);

	/**
	 * 批量设置场地分组
	 * @return
	 */
	ApiResponse<?> batchGroupingItemCourt(Long gameId);
}

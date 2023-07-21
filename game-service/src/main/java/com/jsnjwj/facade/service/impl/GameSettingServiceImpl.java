package com.jsnjwj.facade.service.impl;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.entity.TcGameArea;
import com.jsnjwj.facade.entity.TcGameAreaItem;
import com.jsnjwj.facade.manager.GameGroupingManager;
import com.jsnjwj.facade.query.GameGroupingSetNumQuery;
import com.jsnjwj.facade.query.GameGroupingAreaSetQuery;
import com.jsnjwj.facade.query.GameGroupingSetQuery;
import com.jsnjwj.facade.service.GameSettingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 合同比对service
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class GameSettingServiceImpl implements GameSettingService {

	@Resource
	private GameGroupingManager gameGroupingManager;

	@Override
	public ApiResponse<Boolean> setCourtNum(GameGroupingSetNumQuery query) {
		if (query.getAreaNum() <= 0)
			return ApiResponse.error("请输入正确的场地数");
		int courtNum = 1;
		gameGroupingManager.resetCourt(query.getGameId());

		List<TcGameArea> areas = new ArrayList<>();
		while (courtNum <= query.getAreaNum()) {
			TcGameArea area = new TcGameArea();
			area.setGameId(query.getGameId());
			area.setAreaName("场地" + courtNum);
			area.setAreaNo(courtNum);
			area.setStatus(1);
			areas.add(area);
			courtNum++;
		}
		gameGroupingManager.saveCourts(areas);
		return ApiResponse.success(true);
	}
	@Override
	public ApiResponse<Boolean> saveCourt(GameGroupingAreaSetQuery query) {
		TcGameArea area = new TcGameArea();
		area.setId(query.getAreaId());
		area.setGameId(query.getGameId());
		area.setAreaName(query.getAreaName());
		area.setStatus(query.getStatus());
		gameGroupingManager.saveCourt(area);
		return ApiResponse.success(true);
	}

	@Override
	public ApiResponse<List<TcGameArea>> getCourts(Long gameId) {
		List<TcGameArea> response = gameGroupingManager.getCourts(gameId);
		return ApiResponse.success(response);
	}

	@Override
	public ApiResponse<Boolean> setGrouping(GameGroupingSetQuery query){
		gameGroupingManager.resetGrouping(query);

		List<TcGameAreaItem> areaItems = new ArrayList<>();
		if (!query.getItemIds().isEmpty()){
			Integer sort = 1;
			for(Long i :query.getItemIds()){
				TcGameAreaItem item = new TcGameAreaItem();
				item.setGameId(query.getGameId());
				item.setItemId(i);
				item.setAreaNo(query.getAreaNo());
				item.setAreaId(query.getAreaId());
				item.setSort(sort);
				areaItems.add(item);
				sort++;
			}
		}
		gameGroupingManager.saveGroupings(areaItems);

		return ApiResponse.success(true);
	}

}

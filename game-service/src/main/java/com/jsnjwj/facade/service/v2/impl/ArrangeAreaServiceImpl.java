package com.jsnjwj.facade.service.v2.impl;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.entity.GameAreaEntity;
import com.jsnjwj.facade.manager.*;
import com.jsnjwj.facade.query.GameGroupingAreaSetQuery;
import com.jsnjwj.facade.query.GameGroupingSetNumQuery;
import com.jsnjwj.facade.service.v2.ArrangeAreaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 场地安排arrange/area
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ArrangeAreaServiceImpl implements ArrangeAreaService {

	private final GameGroupingManager gameGroupingManager;

	@Override
	public ApiResponse<?> setAreaNum(GameGroupingSetNumQuery query) {
		if (query.getAreaNum() <= 0)
			return ApiResponse.error("请输入正确的场地数");
		int courtNum = 1;
		gameGroupingManager.resetCourt(query.getGameId());

		List<GameAreaEntity> areas = new ArrayList<>();
		while (courtNum <= query.getAreaNum()) {
			GameAreaEntity area = new GameAreaEntity();
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
	public ApiResponse<Boolean> saveArea(GameGroupingAreaSetQuery query) {
		GameAreaEntity area = new GameAreaEntity();
		area.setId(query.getAreaId());
		area.setGameId(query.getGameId());
		area.setAreaName(query.getAreaName());
		area.setStatus(query.getStatus());
		gameGroupingManager.saveCourt(area);
		return ApiResponse.success(true);
	}

	@Override
	public ApiResponse<List<GameAreaEntity>> getAreas(Long gameId) {
		List<GameAreaEntity> response = gameGroupingManager.getCourts(gameId);
		return ApiResponse.success(response);
	}

}

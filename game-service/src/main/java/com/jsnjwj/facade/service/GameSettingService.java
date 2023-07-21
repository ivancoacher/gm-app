package com.jsnjwj.facade.service;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.entity.TcGameArea;
import com.jsnjwj.facade.query.GameGroupingSetNumQuery;
import com.jsnjwj.facade.query.GameGroupingAreaSetQuery;
import com.jsnjwj.facade.query.GameGroupingSetQuery;

import java.util.List;

public interface GameSettingService {

	ApiResponse<Boolean> setCourtNum(GameGroupingSetNumQuery query);

	ApiResponse<Boolean> saveCourt(GameGroupingAreaSetQuery query);

	ApiResponse<List<TcGameArea>> getCourts(Long gameId);

	ApiResponse<Boolean> setGrouping(GameGroupingSetQuery query);
}

package com.jsnjwj.facade.service.v2;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.entity.GameAreaEntity;
import com.jsnjwj.facade.query.GameGroupingAreaSetQuery;
import com.jsnjwj.facade.query.GameGroupingSetNumQuery;

import java.util.List;

public interface ArrangeAreaService {
    ApiResponse<?> setCourtNum(GameGroupingSetNumQuery query);

    ApiResponse<Boolean> saveCourt(GameGroupingAreaSetQuery query);

    ApiResponse<List<GameAreaEntity>> getCourts(Long gameId);
}

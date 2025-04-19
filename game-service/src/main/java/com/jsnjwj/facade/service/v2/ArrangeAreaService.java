package com.jsnjwj.facade.service.v2;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.dto.AreaSessionDto;
import com.jsnjwj.facade.dto.ArrangeAreaSessionDto;
import com.jsnjwj.facade.dto.SessionChooseDto;
import com.jsnjwj.facade.query.GameGroupingAreaSetQuery;
import com.jsnjwj.facade.query.GameGroupingSetNumQuery;
import com.jsnjwj.facade.query.ManualDrawAreaSessionBatchQuery;

import java.util.List;

public interface ArrangeAreaService {

	ApiResponse<?> setAreaNum(GameGroupingSetNumQuery query);

	ApiResponse<Boolean> saveArea(GameGroupingAreaSetQuery query);

	ApiResponse<List<ArrangeAreaSessionDto>> getAreas(Long gameId);

	ApiResponse<List<AreaSessionDto>> selectSessionList(GameGroupingAreaSetQuery query);

	ApiResponse<AreaSessionDto> selectUnSessionList(GameGroupingAreaSetQuery query);

	ApiResponse<Boolean> arrangeSessionRandom(ManualDrawAreaSessionBatchQuery query);

	ApiResponse<Boolean> arrangeSessionSave(ManualDrawAreaSessionBatchQuery query);
}

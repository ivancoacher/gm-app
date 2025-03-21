package com.jsnjwj.facade.service.v2;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.dto.SessionItemDto;
import com.jsnjwj.facade.query.session.*;
import com.jsnjwj.facade.vo.session.SessionItemVo;

import java.util.List;

public interface ArrangeSessionItemService {

	ApiResponse<?> getUnSelectedItem(SessionItemGetQuery query);

	ApiResponse<SessionItemDto> getSelectedItem(SessionItemGetQuery query);

	ApiResponse<List<SessionItemDto>> getSelectedItemList(SessionItemGetQuery query);

	ApiResponse<Boolean> saveSessionItem(SessionItemSetQuery query);

	ApiResponse<Boolean> saveSessionItemBatch(SessionItemSetBatchQuery query);

}

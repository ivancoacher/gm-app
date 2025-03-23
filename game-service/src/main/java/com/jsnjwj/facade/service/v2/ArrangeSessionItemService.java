package com.jsnjwj.facade.service.v2;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.dto.SessionItemDto;
import com.jsnjwj.facade.query.session.SessionItemGetQuery;
import com.jsnjwj.facade.query.session.SessionItemSetBatchQuery;
import com.jsnjwj.facade.query.session.SessionItemSetQuery;

import java.util.List;

public interface ArrangeSessionItemService {

    ApiResponse<?> getUnSelectedItem(SessionItemGetQuery query);

    ApiResponse<SessionItemDto> getSelectedItem(SessionItemGetQuery query);

    ApiResponse<List<SessionItemDto>> getSelectedItemList(SessionItemGetQuery query);

    ApiResponse<Boolean> saveSessionItem(SessionItemSetQuery query);

    ApiResponse<Boolean> saveSessionItemBatch(SessionItemSetBatchQuery query);

    ApiResponse<Boolean> saveSessionItemRandom(SessionItemSetBatchQuery query);

}

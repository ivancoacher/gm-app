package com.jsnjwj.facade.service.v2;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.dto.SessionItemDto;
import com.jsnjwj.facade.query.session.GameGroupingSessionSetNumQuery;
import com.jsnjwj.facade.query.session.GameGroupingSessionSetQuery;
import com.jsnjwj.facade.query.session.SessionItemGetQuery;
import com.jsnjwj.facade.query.session.SessionItemSetQuery;
import com.jsnjwj.facade.vo.session.SessionItemVo;

import java.util.List;

public interface ArrangeSessionItemService {

    ApiResponse<?> getUnSelectedItem(SessionItemGetQuery query);

    ApiResponse<SessionItemDto> getSelectedItem(SessionItemGetQuery query);

    ApiResponse<Boolean> saveSessionItem(SessionItemSetQuery query);
}

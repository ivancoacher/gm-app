package com.jsnjwj.facade.service.v2;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.dto.ArrangeSessionInfoDto;
import com.jsnjwj.facade.dto.ArrangeSessionVo;
import com.jsnjwj.facade.query.session.GameGroupingSessionSetNumQuery;
import com.jsnjwj.facade.query.session.GameGroupingSessionSetQuery;

import java.util.List;

public interface ArrangeSessionService {

    ApiResponse<?> setSessionNum(GameGroupingSessionSetNumQuery query);

    ApiResponse<?> addSession(Long gameId);

    ApiResponse<?> deleteSession(GameGroupingSessionSetQuery query);

    ApiResponse<Boolean> saveSession(GameGroupingSessionSetQuery query);

    ApiResponse<List<ArrangeSessionVo>> getSessions(Long gameId);

    ApiResponse<ArrangeSessionInfoDto> getSessionInfo(Long gameId);

}

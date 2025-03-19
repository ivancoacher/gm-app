package com.jsnjwj.facade.service.v2;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.entity.GameSessionEntity;
import com.jsnjwj.facade.query.session.GameGroupingSessionSetNumQuery;
import com.jsnjwj.facade.query.session.GameGroupingSessionSetQuery;

import java.util.List;

public interface ArrangeSessionService {

	ApiResponse<?> setSessionNum(GameGroupingSessionSetNumQuery query);
	ApiResponse<?> addSession(Long gameId);
	ApiResponse<?> deleteSession(GameGroupingSessionSetQuery query);

	ApiResponse<Boolean> saveSession(GameGroupingSessionSetQuery query);

	ApiResponse<List<GameSessionEntity>> getSessions(Long gameId);

}

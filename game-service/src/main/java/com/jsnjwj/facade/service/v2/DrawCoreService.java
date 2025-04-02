package com.jsnjwj.facade.service.v2;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.query.session.ManualDrawBatchQuery;
import com.jsnjwj.facade.query.session.ManualDrawQuery;
import com.jsnjwj.facade.query.session.SystemDrawQuery;

public interface DrawCoreService {

	void drawBySessionId(Long gameId, Long sessionId);

}

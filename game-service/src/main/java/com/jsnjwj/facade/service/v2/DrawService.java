package com.jsnjwj.facade.service.v2;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.query.session.ManualDrawQuery;
import com.jsnjwj.facade.query.session.SystemDrawQuery;

public interface DrawService {

    ApiResponse<?> systemDraw(SystemDrawQuery query);

    ApiResponse<?> getDrawList(SystemDrawQuery query);

    ApiResponse<?> getDraw(SystemDrawQuery query);

    ApiResponse<?> manualDraw(ManualDrawQuery query);
}

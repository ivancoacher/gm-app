package com.jsnjwj.facade.service;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.query.SignSingleListQuery;

public interface SignApplyService {

    ApiResponse<?> fetchSinglePage(SignSingleListQuery query);
}

package com.jsnjwj.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.user.entity.OptLog;
import com.jsnjwj.user.request.FetchOptLogRequest;
import com.jsnjwj.user.request.LoginRequest;

public interface UserService {

	ApiResponse<String> login(LoginRequest request);

	ApiResponse<String> register(LoginRequest request);

	ApiResponse info(Long userId);

	ApiResponse<Page<OptLog>> fetchOptLogList(FetchOptLogRequest request);

}

package com.jsnjwj.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.user.request.FetchOptLogRequest;
import com.jsnjwj.user.request.LoginRequest;
import com.jsnjwj.user.vo.OperateLogVo;

public interface UserService {

	ApiResponse<String> login(LoginRequest request);

	ApiResponse<String> register(LoginRequest request);

	ApiResponse info(Long userId);

	ApiResponse<Page<OperateLogVo>> fetchOptLogList(FetchOptLogRequest request);

}

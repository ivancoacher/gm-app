package com.jsnjwj.user.service;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.user.request.LoginRequest;

public interface UserService {

	ApiResponse<String> login(LoginRequest request);

	ApiResponse<String> register(LoginRequest request);

	ApiResponse info(Long userId);
}

package com.jsnjwj.user.service;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.user.entity.UserAccount;

public interface AccountService {

    ApiResponse<UserAccount> fetch(Long userId);

}

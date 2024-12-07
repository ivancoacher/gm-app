package com.jsnjwj.user.service.impl;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.user.dao.UserAccountDao;
import com.jsnjwj.user.entity.UserAccount;
import com.jsnjwj.user.service.AccountService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    private UserAccountDao userAccountDao;

    @Override
    public ApiResponse<UserAccount> fetch(Long userId) {
        return ApiResponse.success(userAccountDao.selectById(userId));
    }

}

package com.jsnjwj.api.controller;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.common.utils.ThreadLocalUtil;
import com.jsnjwj.user.entity.UserAccount;
import com.jsnjwj.user.request.LoginRequest;
import com.jsnjwj.user.service.AccountService;
import com.jsnjwj.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RestController
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private AccountService accountService;

    @ResponseBody
    @PostMapping("/login")
    public ApiResponse login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }

    @ResponseBody
    @PostMapping("/register")
    public ApiResponse register(@RequestBody LoginRequest request) {
        ApiResponse<Map> response = new ApiResponse<Map>();
        response.setCode(20000);
        return userService.register(request);
    }

    @RequestMapping("/info")
    @ResponseBody
    public ApiResponse info(HttpServletRequest request) {
        return userService.info(ThreadLocalUtil.getCurrentUserId());
    }

    @RequestMapping("/account/info")
    @ResponseBody
    public ApiResponse<UserAccount> accountInfo(HttpServletRequest request) {
        return accountService.fetch(ThreadLocalUtil.getCurrentUserId());
    }

}

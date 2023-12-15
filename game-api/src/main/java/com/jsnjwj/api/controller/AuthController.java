package com.jsnjwj.api.controller;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.common.utils.ThreadLocalUtil;
import com.jsnjwj.user.request.LoginRequest;
import com.jsnjwj.user.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RequestMapping("/auth")
@RestController
public class AuthController {

    @Resource
    private UserService userService;

    @ResponseBody
    @PostMapping("/login")
    public ApiResponse login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }

    @RequestMapping("/info")
    @ResponseBody
    public ApiResponse info(HttpServletRequest request) {
        return userService.info(ThreadLocalUtil.getCurrentUserId());
    }

    @PostMapping("/logout")
    public ApiResponse logout() {
        return ApiResponse.success();
    }

    @PostMapping("/register")
    public ApiResponse register(@RequestBody LoginRequest request) {
        return userService.register(request);
    }

}

package com.jsnjwj.api.controller;

import com.jsnjwj.compare.response.ApiResponse;
import com.jsnjwj.compare.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class UserController {
    @Resource
    private UserService userService;

    @ResponseBody
    @PostMapping("/login")
    public ApiResponse index() {
        ApiResponse<Map> response = new ApiResponse<Map>();
        response.setCode(20000);
        Map<String, String> result = new HashMap<>();
        result.put("data", "admin-token");
        response.setData(result);
        return response;
    }

    @RequestMapping("/info")
    @ResponseBody
    public ApiResponse info() {
        ApiResponse<Map> response = new ApiResponse<Map>();
        response.setCode(20000);
        Map<String, Object> result = new HashMap<>();
        result.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        result.put("introduction", "I am a super administrator");
        result.put("name", "Super Admin");
        List<String> roles = new ArrayList<>();
        roles.add("admin");
        result.put("roles", roles);
        response.setData(result);
        return response;
    }
}

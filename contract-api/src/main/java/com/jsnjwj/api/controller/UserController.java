package com.jsnjwj.api.controller;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.user.request.LoginRequest;
import com.jsnjwj.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
	public ApiResponse login(@RequestBody LoginRequest request) {
		ApiResponse<Map> response = new ApiResponse<Map>();
		response.setCode(20000);
		Map<String, String> result = new HashMap<>();

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

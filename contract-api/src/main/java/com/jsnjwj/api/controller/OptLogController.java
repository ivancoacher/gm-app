package com.jsnjwj.api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.user.entity.OptLog;
import com.jsnjwj.user.entity.UserAccount;
import com.jsnjwj.user.request.FetchOptLogRequest;
import com.jsnjwj.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping(value = "/optlog")
public class OptLogController {

	@Resource
	private UserService userService;

	@RequestMapping("/list")
	@ResponseBody
	public ApiResponse<Page<OptLog>> accountInfo(FetchOptLogRequest query, HttpServletRequest request) {
		query.setUserId(Integer.valueOf((String) request.getAttribute("identifyId")));
		return userService.fetchOptLogList(query);
	}

}

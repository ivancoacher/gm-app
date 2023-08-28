package com.jsnjwj.api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.common.utils.ThreadLocalUtil;
import com.jsnjwj.user.request.FetchOptLogRequest;
import com.jsnjwj.user.service.UserService;
import com.jsnjwj.user.vo.OperateLogVo;
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
	public ApiResponse<Page<OperateLogVo>> accountInfo(FetchOptLogRequest query, HttpServletRequest request) {
		query.setUserId(ThreadLocalUtil.getCurrentUserId());
		return userService.fetchOptLogList(query);
	}

}

package com.jsnjwj.api.controller;

import com.jsnjwj.compare.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Slf4j
@Controller
public class IndexController {

	@Resource
	private UserService userService;

	@ResponseBody
	@RequestMapping("/")
	public Integer index() {
		return userService.save();
	}

}

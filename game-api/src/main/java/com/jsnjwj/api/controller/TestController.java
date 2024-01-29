package com.jsnjwj.api.controller;

import com.jsnjwj.common.utils.ThreadLocalUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/test")
@RestController
public class TestController {

	@RequestMapping("/")
	public Long getTest() {
		return ThreadLocalUtil.getCurrentUserId();
	}

}

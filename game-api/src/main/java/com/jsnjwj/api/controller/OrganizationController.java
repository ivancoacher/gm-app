package com.jsnjwj.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping("/org")

public class OrganizationController {

	@ResponseBody
	@RequestMapping("/")
	public Integer index() {
		return 2222;
	}

}

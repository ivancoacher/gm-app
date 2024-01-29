package com.jsnjwj.api.controller;

import com.jsnjwj.common.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 报名人员场序配置
 */
@RestController
@RequestMapping("/game/sign/arrange/sequence")
public class SignArrangeSequenceController {

	/**
	 * 场序列表
	 * @return
	 */
	@GetMapping("/list")
	public ApiResponse<?> getList() {
		return null;
	}

	/**
	 * 设置场序
	 * @return
	 */
	@PostMapping("/set")
	public ApiResponse<?> setSequence() {
		return null;
	}

	/**
	 * 查看场序详情
	 * @return
	 */
	@GetMapping
	public ApiResponse<?> fetchSequenceInfo() {
		return null;
	}

}

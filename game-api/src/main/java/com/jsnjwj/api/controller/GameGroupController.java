package com.jsnjwj.api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.facade.service.GameGroupService;
import com.jsnjwj.facade.vo.GameGroupVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/game/group")
public class GameGroupController {

	@Resource
	private GameGroupService gameGroupService;

	@RequestMapping("/list")
	public Page<GameGroupVo> fetchList() {
		// return gameGroupService.fetchList();
		return null;
	}

}

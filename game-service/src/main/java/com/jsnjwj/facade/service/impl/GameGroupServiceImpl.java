package com.jsnjwj.facade.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.facade.manager.GameGroupManager;
import com.jsnjwj.facade.query.GameGroupListQuery;
import com.jsnjwj.facade.service.GameGroupService;
import com.jsnjwj.facade.vo.GameGroupAllVo;
import com.jsnjwj.facade.vo.GameGroupVo;
import java.util.Collections;

import com.jsnjwj.facade.vo.GroupLabelVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GameGroupServiceImpl implements GameGroupService {

	@Resource
	private GameGroupManager groupManager;

	/**
	 * 分页查询
	 * @return
	 */
	@Override
	public Page<GroupLabelVo> fetchPages(GameGroupListQuery query) {

		return groupManager.fetchGroupPage(query.getGameId());
	}

	/**
	 * 查询全部
	 * @return
	 */
	@Override
	public List<GroupLabelVo> fetchList(GameGroupListQuery query) {
		return groupManager.fetchGroups(query.getGameId());
	}

	/**
	 * 查询包含项目数据
	 * @return
	 */
	@Override
	public List<GameGroupAllVo> fetchAll(Long gameId) {
		return null;
	}

}

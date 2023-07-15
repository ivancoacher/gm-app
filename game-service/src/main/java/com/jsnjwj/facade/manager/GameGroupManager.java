package com.jsnjwj.facade.manager;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.facade.entity.TcGameGroup;
import com.jsnjwj.facade.mapper.TcGameGroupMapper;
import com.jsnjwj.facade.vo.GroupLabelVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GameGroupManager {

	private final TcGameGroupMapper gameGroupMapper;

	public List<GroupLabelVo> fetchGroups(Long gameId) {
		LambdaQueryWrapper<TcGameGroup> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(TcGameGroup::getGameId, gameId);
		wrapper.orderByAsc(TcGameGroup::getSort);
		List<TcGameGroup> groups = gameGroupMapper.selectList(wrapper);
		List<GroupLabelVo> response = new ArrayList<>();

		if (CollUtil.isNotEmpty(groups)) {
			groups.forEach(group -> {
				GroupLabelVo groupLabelVo = new GroupLabelVo();
				groupLabelVo.setGameId(gameId);
				groupLabelVo.setGroupName(group.getGroupName());
				groupLabelVo.setGroupId(group.getId());
				groupLabelVo.setSort(group.getSort());
			});
		}

		return response;
	}

	public Page<GroupLabelVo> fetchGroupPage(Long gameId) {
		Page<TcGameGroup> page = new Page<TcGameGroup>();
		LambdaQueryWrapper<TcGameGroup> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(TcGameGroup::getGameId, gameId);
		wrapper.orderByAsc(TcGameGroup::getSort);
		Page<TcGameGroup> groups = gameGroupMapper.selectPage(page, wrapper);

		Page<GroupLabelVo> response = new Page<>();

		if (CollUtil.isNotEmpty(groups.getRecords())) {
			groups.getRecords().forEach(group -> {
				GroupLabelVo groupLabelVo = new GroupLabelVo();
				groupLabelVo.setGameId(gameId);
				groupLabelVo.setGroupName(group.getGroupName());
				groupLabelVo.setGroupId(group.getId());
				groupLabelVo.setSort(group.getSort());
				response.getRecords().add(groupLabelVo);
			});
		}
		response.setPages(page.getPages());
		response.setTotal(page.getTotal());
		response.setCurrent(page.getCurrent());
		response.setSize(page.getSize());

		return response;
	}

	public int saveGroup(TcGameGroup gameGroup) {
		return gameGroupMapper.insert(gameGroup);
	}

	public int updateGroup(TcGameGroup gameGroup) {
		return gameGroupMapper.insert(gameGroup);
	}

	public int deleteGroup(Long groupId) {
		return gameGroupMapper.deleteById(groupId);
	}

}

package com.jsnjwj.facade.manager;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.facade.entity.GameGroupEntity;
import com.jsnjwj.facade.mapper.GameGroupMapper;
import com.jsnjwj.facade.vo.GroupLabelVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameGroupManager {

	private final GameGroupMapper gameGroupMapper;

	public List<GroupLabelVo> fetchGroups(Long gameId) {
		LambdaQueryWrapper<GameGroupEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(GameGroupEntity::getGameId, gameId);
		wrapper.orderByAsc(GameGroupEntity::getSort);
		List<GameGroupEntity> groups = gameGroupMapper.selectList(wrapper);
		List<GroupLabelVo> response = new ArrayList<>();

		if (CollUtil.isNotEmpty(groups)) {
			groups.forEach(group -> {
				GroupLabelVo groupLabelVo = new GroupLabelVo();
				groupLabelVo.setGameId(gameId);
				groupLabelVo.setGroupName(group.getGroupName());
				groupLabelVo.setGroupId(group.getId());
				groupLabelVo.setSort(group.getSort());
				response.add(groupLabelVo);
			});
		}

		return response;
	}

	public Page<GroupLabelVo> fetchGroupPage(Long gameId) {
		Page<GameGroupEntity> page = new Page<GameGroupEntity>();
		LambdaQueryWrapper<GameGroupEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(GameGroupEntity::getGameId, gameId);
		wrapper.orderByAsc(GameGroupEntity::getSort);
		Page<GameGroupEntity> groups = gameGroupMapper.selectPage(page, wrapper);

		Page<GroupLabelVo> response = new Page<>();
		response.setRecords(new ArrayList<>());
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

	public int save(GameGroupEntity gameGroup) {
		gameGroup.setUpdateTime(new Date());
		return gameGroupMapper.insert(gameGroup);
	}

	public int update(GameGroupEntity gameGroup) {
		return gameGroupMapper.updateById(gameGroup);
	}

	public int deleteGroup(Long groupId) {
		return gameGroupMapper.deleteById(groupId);
	}

	public GameGroupEntity fetchOneInfo(Long gameId, Long groupId) {
		LambdaQueryWrapper<GameGroupEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(GameGroupEntity::getGameId, gameId);
		wrapper.eq(GameGroupEntity::getId, groupId);
		return gameGroupMapper.selectOne(wrapper);
	}

}

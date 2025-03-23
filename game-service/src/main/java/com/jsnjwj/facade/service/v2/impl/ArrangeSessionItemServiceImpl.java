package com.jsnjwj.facade.service.v2.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.dto.SessionItemDto;
import com.jsnjwj.facade.entity.GameItemEntity;
import com.jsnjwj.facade.entity.GameSessionEntity;
import com.jsnjwj.facade.entity.GameSessionItemEntity;
import com.jsnjwj.facade.manager.ArrangeSessionItemManager;
import com.jsnjwj.facade.manager.GameGroupManager;
import com.jsnjwj.facade.manager.GameItemManager;
import com.jsnjwj.facade.mapper.GameSessionMapper;
import com.jsnjwj.facade.query.session.SessionItemGetQuery;
import com.jsnjwj.facade.query.session.SessionItemSetBatchQuery;
import com.jsnjwj.facade.query.session.SessionItemSetQuery;
import com.jsnjwj.facade.service.v2.ArrangeSessionItemService;
import com.jsnjwj.facade.vo.GroupLabelVo;
import com.jsnjwj.facade.vo.session.SessionItemVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 场地安排
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ArrangeSessionItemServiceImpl implements ArrangeSessionItemService {

	private final ArrangeSessionItemManager arrangeSessionItemManager;

	private final GameItemManager gameItemManager;

	private final GameSessionMapper gameSessionMapper;

	private final GameGroupManager gameGroupManager;

	/**
	 * 查询剩余未排场次项目
	 * @param query
	 * @return
	 */
	@Override
	public ApiResponse<?> getUnSelectedItem(SessionItemGetQuery query) {
		// 查询已排场次项目
		List<Long> selectedItemIds = arrangeSessionItemManager.selectAllSelectedItemIds(query.getGameId());
		// 排除已排
		LambdaQueryWrapper<GameItemEntity> query1 = new LambdaQueryWrapper<>();
		query1.eq(GameItemEntity::getGameId, query.getGameId());
		if (CollectionUtil.isNotEmpty(selectedItemIds)) {
			query1.notIn(GameItemEntity::getId, selectedItemIds);
		}

		if (Objects.nonNull(query.getGroupId())) {
			query1.eq(GameItemEntity::getGroupId, query.getGroupId());
		}

		if (Objects.nonNull(query.getItemId())) {
			query1.eq(GameItemEntity::getId, query.getItemId());
		}

		SessionItemDto sessionItemVo = new SessionItemDto();
		sessionItemVo.setGameId(query.getGameId());
		sessionItemVo.setSessionName("未排项目");
		List<GameItemEntity> list = gameItemManager.fetchListByWrapper(query1);

		List<GroupLabelVo> groupEntities = gameGroupManager.fetchGroups(query.getGameId());
		List<SessionItemVo> response = new ArrayList<>();
		if (CollectionUtil.isNotEmpty(groupEntities)) {
			Map<Long, GroupLabelVo> groupLabelVoMap = groupEntities.stream()
				.collect(Collectors.toMap(GroupLabelVo::getGroupId, group -> group));
			response = list.stream().map(item -> {
				SessionItemVo vo = new SessionItemVo();
				vo.setGroupName(groupLabelVoMap.get(item.getGroupId()).getGroupName());
				vo.setItemId(item.getId());
				vo.setSessionId(0);
				vo.setItemName(item.getItemName());
				return vo;
			}).collect(Collectors.toList());
		}
		sessionItemVo.setData(response);
		return ApiResponse.success(sessionItemVo);
	}

	/**
	 * 查询当前场次已排项目
	 * @param query
	 * @return
	 */
	@Override
	public ApiResponse<List<SessionItemDto>> getSelectedItemList(SessionItemGetQuery query) {

		List<GameSessionEntity> sessionEntities = gameSessionMapper.selectList(
				new LambdaQueryWrapper<GameSessionEntity>().eq(GameSessionEntity::getGameId, query.getGameId()));
		List<SessionItemDto> response = new ArrayList<>();
		if (CollectionUtil.isNotEmpty(sessionEntities)) {
			for (GameSessionEntity session : sessionEntities) {
				SessionItemDto sessionItemDto = new SessionItemDto();
				sessionItemDto.setGameId(query.getGameId());
				sessionItemDto.setSessionId(session.getId());
				List<SessionItemVo> list = arrangeSessionItemManager.fetchBySessionId(query.getGameId(),
						session.getId());
				sessionItemDto.setData(list);
				sessionItemDto.setSessionName(session.getSessionName());
				response.add(sessionItemDto);
			}
		}

		return ApiResponse.success(response);
	}

	@Override
	public ApiResponse<SessionItemDto> getSelectedItem(SessionItemGetQuery query) {

		SessionItemDto sessionItemDto = new SessionItemDto();
		sessionItemDto.setGameId(query.getGameId());
		List<SessionItemVo> list = arrangeSessionItemManager.fetchBySessionId(query.getGameId(), query.getSessionId());
		sessionItemDto.setData(list);
		GameSessionEntity session = gameSessionMapper.selectById(query.getSessionId());
		sessionItemDto.setSessionName(session.getSessionName());
		sessionItemDto.setSessionId(session.getId());

		return ApiResponse.success(sessionItemDto);
	}

	/**
	 * 保存场次-项目关系
	 * @param query
	 * @return
	 */
	@Override
	public ApiResponse<Boolean> saveSessionItem(SessionItemSetQuery query) {

		arrangeSessionItemManager.deleteBySessionId(query.getGameId(), query.getSessionId());

		List<GameSessionItemEntity> result = new ArrayList<>();
		if (CollectionUtil.isNotEmpty(query.getData())) {
			query.getData().forEach(item -> {
				if (Objects.isNull(item.getItemId())) {
					return;
				}
				GameSessionItemEntity gameSessionItemEntity = new GameSessionItemEntity();
				gameSessionItemEntity.setGameId(query.getGameId());
				gameSessionItemEntity.setSessionId(query.getSessionId());
				gameSessionItemEntity.setItemId(item.getItemId());
				gameSessionItemEntity.setSort(item.getSort());
				gameSessionItemEntity.setCreatedAt(new Date());
				result.add(gameSessionItemEntity);
			});

			arrangeSessionItemManager.saveBatch(result);
		}

		return ApiResponse.success(true);
	}

	/**
	 * 批量保存场次-项目关系
	 * @param query
	 * @return
	 */
	@Override
	public ApiResponse<Boolean> saveSessionItemBatch(SessionItemSetBatchQuery query) {

		arrangeSessionItemManager.deleteBySessionId(query.getGameId(), null);

		List<GameSessionItemEntity> result = new ArrayList<>();
		if (CollectionUtil.isNotEmpty(query.getData())) {
			query.getData().forEach(session -> {
				session.getData().forEach(item -> {
					GameSessionItemEntity gameSessionItemEntity = new GameSessionItemEntity();
					gameSessionItemEntity.setGameId(query.getGameId());
					gameSessionItemEntity.setSessionId(session.getSessionId());
					gameSessionItemEntity.setItemId(item.getItemId());
					gameSessionItemEntity.setSort(item.getSort());
					gameSessionItemEntity.setCreatedAt(new Date());
					result.add(gameSessionItemEntity);
				});
			});
			if (CollectionUtil.isNotEmpty(result)) {
				arrangeSessionItemManager.saveBatch(result);
			}
		}

		return ApiResponse.success(true);
	}

	@Override
	public ApiResponse<Boolean> saveSessionItemRandom(SessionItemSetBatchQuery query) {
		Long gameId = query.getGameId();
		List<GameSessionEntity> sessionEntities = gameSessionMapper.selectList(
				new LambdaQueryWrapper<GameSessionEntity>().eq(GameSessionEntity::getGameId, query.getGameId()));
		if (CollectionUtil.isEmpty(sessionEntities)) {
			return ApiResponse.error("请先设置场次");
		}
		// 清空所有场次项目
		arrangeSessionItemManager.deleteBySessionId(query.getGameId(), null);

		List<GameItemEntity> itemList = gameItemManager.fetchListByGameId(gameId);
		if (CollectionUtil.isEmpty(itemList)) {
			return ApiResponse.error("请先导入赛事项目");
		}

		Map<Long, List<GameItemEntity>> allocateResult = allocateAllProjects(itemList, sessionEntities);
		log.info("分配结果：{}", JSON.toJSONString(allocateResult));
		if (Objects.isNull(allocateResult)) {
			return ApiResponse.error("随机分配失败");
		}
		List<GameSessionItemEntity> result = new ArrayList<>();
		allocateResult.forEach((sessionId, itemEntities) -> {
			AtomicInteger sort = new AtomicInteger();
			itemEntities.forEach(item -> {
				sort.getAndIncrement();
				GameSessionItemEntity gameSessionItemEntity = new GameSessionItemEntity();
				gameSessionItemEntity.setGameId(query.getGameId());
				gameSessionItemEntity.setSessionId(sessionId);
				gameSessionItemEntity.setItemId(item.getId());
				gameSessionItemEntity.setSort(sort.get());
				gameSessionItemEntity.setCreatedAt(new Date());
				result.add(gameSessionItemEntity);
			});
		});
		arrangeSessionItemManager.saveBatch(result);

		return ApiResponse.success(true);
	}

	public Map<Long, List<GameItemEntity>> allocateAllProjects(List<GameItemEntity> projects,
			List<GameSessionEntity> sessions) {
		// 验证输入参数
		if (sessions == null || sessions.isEmpty()) {
			throw new IllegalArgumentException("场次列表不能为空");
		}
		if (projects == null || projects.isEmpty()) {
			throw new IllegalArgumentException("项目列表不能为空");
		}

		// 创建sessionId到项目列表的映射
		Map<Long, List<GameItemEntity>> allocation = new HashMap<>();
		for (GameSessionEntity session : sessions) {
			allocation.put(session.getId(), new ArrayList<>());
		}

		// 复制项目列表并随机打乱
		List<GameItemEntity> shuffledProjects = new ArrayList<>(projects);
		Collections.shuffle(shuffledProjects, new Random());

		// 计算基本每个场次应该分配的项目数量和剩余项目数
		int sessionCount = sessions.size();
		int baseProjectsPerSession = shuffledProjects.size() / sessionCount;
		int remainingProjects = shuffledProjects.size() % sessionCount;

		// 分配项目到场次
		int projectIndex = 0;
		for (GameSessionEntity session : sessions) {
			Long sessionId = session.getId();

			// 分配基本数量的项目
			for (int i = 0; i < baseProjectsPerSession; i++) {
				if (projectIndex < shuffledProjects.size()) {
					allocation.get(sessionId).add(shuffledProjects.get(projectIndex++));
				}
			}

			// 如果有剩余项目，额外分配一个
			if (remainingProjects > 0) {
				if (projectIndex < shuffledProjects.size()) {
					allocation.get(sessionId).add(shuffledProjects.get(projectIndex++));
				}
				remainingProjects--;
			}
		}

		return allocation;
	}

}

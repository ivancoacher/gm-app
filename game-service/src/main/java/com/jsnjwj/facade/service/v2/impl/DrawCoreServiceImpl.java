package com.jsnjwj.facade.service.v2.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsnjwj.facade.entity.*;
import com.jsnjwj.facade.manager.ArrangeDrawManager;
import com.jsnjwj.facade.manager.ArrangeSessionItemManager;
import com.jsnjwj.facade.manager.ArrangeSessionManager;
import com.jsnjwj.facade.manager.SignApplyManager;
import com.jsnjwj.facade.service.v2.DrawCoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 抽签分组
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DrawCoreServiceImpl implements DrawCoreService {

	private final ArrangeSessionManager sessionManager;

	private final ArrangeSessionItemManager sessionItemManager;

	private final SignApplyManager signApplyManager;

	private final ArrangeDrawManager drawManager;

	@Override
	public void drawBySessionId(Long sessionId) {
		GameSessionEntity session = sessionManager.getBySessionId(sessionId);

		if (Objects.isNull(session)) {
			log.info("该场次不存在");
			return;
		}
		Long gameId = session.getGameId();
		List<GameSessionItemEntity> items = sessionItemManager.fetchListBySessionId(session.getGameId(), sessionId);
		if (CollectionUtil.isEmpty(items)) {
			log.info("该场次没有场次项目");
			return;
		}
		List<Long> itemIds = items.stream().map(GameSessionItemEntity::getItemId).collect(Collectors.toList());
		List<SignSingleEntity> singleList = signApplyManager.getSingleByItemIds(session.getGameId(), itemIds);

		if (CollectionUtil.isEmpty(singleList)) {
			log.info("该场次没有报名人员");
			return;
		}

		// 场次配置
		GameSessionSettingEntity settingEntity = sessionManager.getSettingBySessionId(session.getGameId(), sessionId);
		if (Objects.isNull(settingEntity)) {
			settingEntity = new GameSessionSettingEntity();
		}

		int orgRangeMin = settingEntity.getOrgMin();
		int orgRangeMax = settingEntity.getOrgMax();
		int teamRangeMin = settingEntity.getTeamMin();
		int teamRangeMax = settingEntity.getTeamMax();
		int singleRangeMin = settingEntity.getSingleMin();
		int singleRangeMax = settingEntity.getSingleMax();

		List<GameDrawEntity> orderList = generateDrawOrder(sessionId, singleList, orgRangeMin, orgRangeMax,
				teamRangeMin, teamRangeMax, singleRangeMin, singleRangeMax);

		if (CollectionUtil.isEmpty(orderList)) {
			return;
		}

		drawManager.clearDraw(gameId, sessionId);

		AtomicInteger sort = new AtomicInteger();
		orderList.forEach(gameDrawEntity -> {
			sort.getAndIncrement();
			gameDrawEntity.setGameId(gameId);
			gameDrawEntity.setSort(sort.get());
			gameDrawEntity.setCreatedAt(new Date());
		});

		drawManager.saveBatch(orderList);
	}

	private List<GameDrawEntity> generateDrawOrder(Long sessionId, List<SignSingleEntity> singleList, int orgRangeMin,
			int orgRangeMax, int teamRangeMin, int teamRangeMax, int singleRangeMin, int singleRangeMax) {

		List<GameDrawEntity> result = new ArrayList<>();
		Map<Long, Integer> orgLastIndex = new HashMap<>(); // 记录每个组织最后出现的索引
		Map<Long, Integer> teamLastIndex = new HashMap<>(); // 记录每个队伍最后出现的索引
		Map<Long, Integer> playerLastIndex = new HashMap<>(); // 记录每个选手最后出现的索引

		Map<Long, List<SignSingleEntity>> orgPlayers = new HashMap<>();
		Map<Long, List<SignSingleEntity>> teamPlayers = new HashMap<>();
		Map<Long, List<SignSingleEntity>> singlePlayers = new HashMap<>();

		for (SignSingleEntity player : singleList) {
			orgPlayers.computeIfAbsent(player.getOrgId(), k -> new ArrayList<>()).add(player);
			teamPlayers.computeIfAbsent(player.getTeamId(), k -> new ArrayList<>()).add(player);
			singlePlayers.computeIfAbsent(player.getId(), k -> new ArrayList<>()).add(player);
		}
		// 计算每个组织的最大间隔
		Map<Long, Integer> orgMaxRange = new HashMap<>();
		for (Map.Entry<Long, List<SignSingleEntity>> entry : orgPlayers.entrySet()) {
			int groupSize = entry.getValue().size();
			if (groupSize <= orgRangeMin) {
				orgMaxRange.put(entry.getKey(), singleList.size() / groupSize); // 均匀分布
			}
			else {
				orgMaxRange.put(entry.getKey(), orgRangeMax);
			}
		}

		// 计算每个队伍的最大间隔
		Map<Long, Integer> teamMaxRange = new HashMap<>();
		for (Map.Entry<Long, List<SignSingleEntity>> entry : teamPlayers.entrySet()) {
			int groupSize = entry.getValue().size();
			if (groupSize <= teamRangeMin) {
				teamMaxRange.put(entry.getKey(), singleList.size() / groupSize); // 均匀分布
			}
			else {
				teamMaxRange.put(entry.getKey(), teamRangeMax);
			}
		}

		// 计算每个选手的最大间隔
		Map<Long, Integer> singleMaxRange = new HashMap<>();
		for (Map.Entry<Long, List<SignSingleEntity>> entry : singlePlayers.entrySet()) {
			int groupSize = entry.getValue().size();
			if (groupSize <= singleRangeMin) {
				singleMaxRange.put(entry.getKey(), singleList.size() / groupSize); // 均匀分布
			}
			else {
				singleMaxRange.put(entry.getKey(), singleRangeMax);
			}
		}
		// 1. 随机打乱参与人员
		List<SignSingleEntity> shuffledPlayers = new ArrayList<>(singleList);
		Collections.shuffle(shuffledPlayers, new Random());

		// 尝试抽签，逐步放宽条件
		boolean success = false;
		while (!success) {
			try {
				result.clear();
				orgLastIndex.clear();
				teamLastIndex.clear();
				playerLastIndex.clear();

				for (int i = 0; i < shuffledPlayers.size(); i++) {
					SignSingleEntity player = shuffledPlayers.get(i);
					boolean isValid = true;

					// 检查组织间隔
					if (orgLastIndex.containsKey(player.getOrgId())) {
						int lastIndex = orgLastIndex.get(player.getOrgId());
						if (i - lastIndex < orgRangeMin) {
							isValid = false;
						}
					}

					// 检查队伍间隔
					if (teamLastIndex.containsKey(player.getTeamId())) {
						int lastIndex = teamLastIndex.get(player.getTeamId());
						if (i - lastIndex < teamRangeMin) {
							isValid = false;
						}
					}

					// 检查选手间隔
					if (playerLastIndex.containsKey(player.getId())) {
						int lastIndex = playerLastIndex.get(player.getId());
						if (i - lastIndex < singleRangeMin) {
							isValid = false;
						}
					}

					if (isValid) {
						GameDrawEntity gameDrawEntity = new GameDrawEntity();
						gameDrawEntity.setSessionId(sessionId);
						gameDrawEntity.setSignId(player.getId());
						result.add(gameDrawEntity);
						orgLastIndex.put(player.getOrgId(), i);
						teamLastIndex.put(player.getTeamId(), i);
						playerLastIndex.put(player.getId(), i);
					}
					else {
						// 如果无法满足条件，抛出异常
						throw new IllegalArgumentException("无法满足间隔要求");
					}
				}

				success = true; // 抽签成功
			}
			catch (IllegalArgumentException e) {
				// 放宽条件
				if (orgRangeMin > 0)
					orgRangeMin--;
				if (teamRangeMin > 0)
					teamRangeMin--;
				if (singleRangeMin > 0)
					singleRangeMin--;

				// 如果所有间隔都已放宽到0，则直接按随机顺序排列
				if (orgRangeMin == 0 && teamRangeMin == 0 && singleRangeMin == 0) {
					result = shuffledPlayers.stream().map(player -> {
						GameDrawEntity gameDrawEntity = new GameDrawEntity();
						gameDrawEntity.setSessionId(sessionId);
						gameDrawEntity.setSignId(player.getId());
						return gameDrawEntity;
					}).collect(Collectors.toList());
					success = true;
				}
			}
		}

		return result;

	}

}

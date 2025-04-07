package com.jsnjwj.facade.service.v2.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsnjwj.facade.dto.SignSingleDto;
import com.jsnjwj.facade.dto.SignTeamDto;
import com.jsnjwj.facade.dto.draw.DrawResultUnit;
import com.jsnjwj.facade.dto.draw.SessionUnit;
import com.jsnjwj.facade.entity.*;
import com.jsnjwj.facade.enums.ItemTypeEnum;
import com.jsnjwj.facade.manager.*;
import com.jsnjwj.facade.mapper.GameDrawMapper;
import com.jsnjwj.facade.service.v2.DrawCoreService;
import com.jsnjwj.facade.vo.GameItemVo;
import com.jsnjwj.facade.vo.session.SessionItemVo;
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
	private final GameItemManager gameItemManager;
	private final GameDrawMapper gameDrawMapper;
	private final ArrangeDrawManager arrangeDrawManager;
//
//	@Override
//	public void drawBySessionId(Long sessionId) {
//		GameSessionEntity session = sessionManager.getBySessionId(sessionId);
//
//		if (Objects.isNull(session)) {
//			log.info("该场次不存在");
//			return;
//		}
//		Long gameId = session.getGameId();
//		List<GameSessionItemEntity> items = sessionItemManager.fetchListBySessionId(session.getGameId(), sessionId);
//		if (CollectionUtil.isEmpty(items)) {
//			log.info("该场次没有场次项目");
//			return;
//		}
//		List<Long> itemIds = items.stream().map(GameSessionItemEntity::getItemId).collect(Collectors.toList());
//		List<SignSingleEntity> singleList = signApplyManager.getSingleByItemIds(session.getGameId(), itemIds);
//
//		if (CollectionUtil.isEmpty(singleList)) {
//			log.info("该场次没有报名人员");
//			return;
//		}
//
//		// 场次配置
//		GameSessionSettingEntity settingEntity = sessionManager.getSettingBySessionId(session.getGameId(), sessionId);
//		if (Objects.isNull(settingEntity)) {
//			settingEntity = new GameSessionSettingEntity();
//		}
//
//		int orgRangeMin = settingEntity.getOrgMin();
//		int orgRangeMax = settingEntity.getOrgMax();
//		int teamRangeMin = settingEntity.getTeamMin();
//		int teamRangeMax = settingEntity.getTeamMax();
//		int singleRangeMin = settingEntity.getSingleMin();
//		int singleRangeMax = settingEntity.getSingleMax();
//
//		List<GameDrawEntity> orderList = generateDrawOrder(sessionId, singleList, orgRangeMin, orgRangeMax,
//				teamRangeMin, teamRangeMax, singleRangeMin, singleRangeMax);
//
//		if (CollectionUtil.isEmpty(orderList)) {
//			return;
//		}
//
//		drawManager.clearDraw(gameId, sessionId);
//
//		AtomicInteger sort = new AtomicInteger();
//		orderList.forEach(gameDrawEntity -> {
//			sort.getAndIncrement();
//			gameDrawEntity.setGameId(gameId);
//			gameDrawEntity.setSort(sort.get());
//			gameDrawEntity.setCreatedAt(new Date());
//		});
//
//		drawManager.saveBatch(orderList);
//	}
//
//	private List<GameDrawEntity> generateDrawOrder(Long sessionId, List<SignSingleEntity> singleList, int orgRangeMin,
//			int orgRangeMax, int teamRangeMin, int teamRangeMax, int singleRangeMin, int singleRangeMax) {
//
//		List<GameDrawEntity> result = new ArrayList<>();
//		Map<Long, Integer> orgLastIndex = new HashMap<>(); // 记录每个组织最后出现的索引
//		Map<Long, Integer> teamLastIndex = new HashMap<>(); // 记录每个队伍最后出现的索引
//		Map<Long, Integer> playerLastIndex = new HashMap<>(); // 记录每个选手最后出现的索引
//
//		Map<Long, List<SignSingleEntity>> orgPlayers = new HashMap<>();
//		Map<Long, List<SignSingleEntity>> teamPlayers = new HashMap<>();
//		Map<Long, List<SignSingleEntity>> singlePlayers = new HashMap<>();
//
//		for (SignSingleEntity player : singleList) {
//			orgPlayers.computeIfAbsent(player.getOrgId(), k -> new ArrayList<>()).add(player);
//			teamPlayers.computeIfAbsent(player.getTeamId(), k -> new ArrayList<>()).add(player);
//			singlePlayers.computeIfAbsent(player.getId(), k -> new ArrayList<>()).add(player);
//		}
//		// 计算每个组织的最大间隔
//		Map<Long, Integer> orgMaxRange = new HashMap<>();
//		for (Map.Entry<Long, List<SignSingleEntity>> entry : orgPlayers.entrySet()) {
//			int groupSize = entry.getValue().size();
//			if (groupSize <= orgRangeMin) {
//				orgMaxRange.put(entry.getKey(), singleList.size() / groupSize); // 均匀分布
//			}
//			else {
//				orgMaxRange.put(entry.getKey(), orgRangeMax);
//			}
//		}
//
//		// 计算每个队伍的最大间隔
//		Map<Long, Integer> teamMaxRange = new HashMap<>();
//		for (Map.Entry<Long, List<SignSingleEntity>> entry : teamPlayers.entrySet()) {
//			int groupSize = entry.getValue().size();
//			if (groupSize <= teamRangeMin) {
//				teamMaxRange.put(entry.getKey(), singleList.size() / groupSize); // 均匀分布
//			}
//			else {
//				teamMaxRange.put(entry.getKey(), teamRangeMax);
//			}
//		}
//
//		// 计算每个选手的最大间隔
//		Map<Long, Integer> singleMaxRange = new HashMap<>();
//		for (Map.Entry<Long, List<SignSingleEntity>> entry : singlePlayers.entrySet()) {
//			int groupSize = entry.getValue().size();
//			if (groupSize <= singleRangeMin) {
//				singleMaxRange.put(entry.getKey(), singleList.size() / groupSize); // 均匀分布
//			}
//			else {
//				singleMaxRange.put(entry.getKey(), singleRangeMax);
//			}
//		}
//		// 1. 随机打乱参与人员
//		List<SignSingleEntity> shuffledPlayers = new ArrayList<>(singleList);
//		Collections.shuffle(shuffledPlayers, new Random());
//
//		// 尝试抽签，逐步放宽条件
//		boolean success = false;
//		while (!success) {
//			try {
//				result.clear();
//				orgLastIndex.clear();
//				teamLastIndex.clear();
//				playerLastIndex.clear();
//
//				for (int i = 0; i < shuffledPlayers.size(); i++) {
//					SignSingleEntity player = shuffledPlayers.get(i);
//					boolean isValid = true;
//
//					// 检查组织间隔
//					if (orgLastIndex.containsKey(player.getOrgId())) {
//						int lastIndex = orgLastIndex.get(player.getOrgId());
//						if (i - lastIndex < orgRangeMin) {
//							isValid = false;
//						}
//					}
//
//					// 检查队伍间隔
//					if (teamLastIndex.containsKey(player.getTeamId())) {
//						int lastIndex = teamLastIndex.get(player.getTeamId());
//						if (i - lastIndex < teamRangeMin) {
//							isValid = false;
//						}
//					}
//
//					// 检查选手间隔
//					if (playerLastIndex.containsKey(player.getId())) {
//						int lastIndex = playerLastIndex.get(player.getId());
//						if (i - lastIndex < singleRangeMin) {
//							isValid = false;
//						}
//					}
//
//					if (isValid) {
//						GameDrawEntity gameDrawEntity = new GameDrawEntity();
//						gameDrawEntity.setSessionId(sessionId);
//						gameDrawEntity.setSignId(player.getId());
//						result.add(gameDrawEntity);
//						orgLastIndex.put(player.getOrgId(), i);
//						teamLastIndex.put(player.getTeamId(), i);
//						playerLastIndex.put(player.getId(), i);
//					}
//					else {
//						// 如果无法满足条件，抛出异常
//						throw new IllegalArgumentException("无法满足间隔要求");
//					}
//				}
//
//				success = true; // 抽签成功
//			}
//			catch (IllegalArgumentException e) {
//				// 放宽条件
//				if (orgRangeMin > 0)
//					orgRangeMin--;
//				if (teamRangeMin > 0)
//					teamRangeMin--;
//				if (singleRangeMin > 0)
//					singleRangeMin--;
//
//				// 如果所有间隔都已放宽到0，则直接按随机顺序排列
//				if (orgRangeMin == 0 && teamRangeMin == 0 && singleRangeMin == 0) {
//					result = shuffledPlayers.stream().map(player -> {
//						GameDrawEntity gameDrawEntity = new GameDrawEntity();
//						gameDrawEntity.setSessionId(sessionId);
//						gameDrawEntity.setSignId(player.getId());
//						return gameDrawEntity;
//					}).collect(Collectors.toList());
//					success = true;
//				}
//			}
//		}
//
//		return result;
//
//	}

	@Override
	public void drawBySessionId(Long gameId, Long sessionId) {
		SessionUnit sessionUnit = new SessionUnit();
		sessionUnit.setGameId(gameId);
		sessionUnit.setSessionId(sessionId);
		List<SessionItemVo> sessionItemVoList = sessionItemManager.fetchBySessionId(gameId,sessionId);
		if (CollectionUtil.isNotEmpty(sessionItemVoList)){
			// 先清除该场次所有抽签结果
			 LambdaQueryWrapper<GameDrawEntity> queryWrapper = new LambdaQueryWrapper<>();
			 queryWrapper.eq(GameDrawEntity::getGameId, gameId);
			 queryWrapper.eq(GameDrawEntity::getSessionId, sessionId);
			 gameDrawMapper.delete(queryWrapper);

			sessionItemVoList.forEach(itemVo -> {
				List<SignSingleEntity> singleList = signApplyManager.getApplyByItem(gameId, itemVo.getItemId());

				itemVo.setSingleList(singleList.stream().map(player -> {
					SignSingleDto signSingleDto = new SignSingleDto();
					signSingleDto.setApplyId(player.getId());
					return signSingleDto;
				}).collect(Collectors.toList()));
				List<SignItemTeamEntity> teamList = signApplyManager.getTeamByItem(gameId, itemVo.getItemId());

				itemVo.setTeamList(teamList.stream().map(player -> {
					SignTeamDto signSingleDto = new SignTeamDto();
					signSingleDto.setTeamId(player.getTeamId());
					return signSingleDto;
				}).collect(Collectors.toList()));
			});
		}
		sessionUnit.setItemList(sessionItemVoList);
		List<DrawResultUnit> result = drawSession(sessionUnit);


		if (CollectionUtil.isNotEmpty(result)){
			List<GameDrawEntity> gameDrawEntityList = new ArrayList<>();
			result.forEach(drawResultUnit -> {
				GameDrawEntity gameDrawEntity = new GameDrawEntity();
				gameDrawEntity.setGameId(gameId);
				gameDrawEntity.setSessionId(sessionId);
				gameDrawEntity.setSort(drawResultUnit.getOrder());
				gameDrawEntity.setItemId(drawResultUnit.getProject().getItemId());
				if (Objects.equals(drawResultUnit.getProject().getItemType(), ItemTypeEnum.TYPE_SINGLE.getType())){
					gameDrawEntity.setSignId(drawResultUnit.getPlayer().getApplyId());
					gameDrawEntity.setDrawType(ItemTypeEnum.TYPE_SINGLE.getType());
				}else{
					gameDrawEntity.setTeamId(drawResultUnit.getTeam().getTeamId());
					gameDrawEntity.setDrawType(ItemTypeEnum.TYPE_TEAM.getType());

				}
				gameDrawEntityList.add(gameDrawEntity);

			});

			arrangeDrawManager.saveBatch(gameDrawEntityList);
		}


		log.info("抽签结果{}", JSON.toJSONString(result));
	}


	public List<DrawResultUnit> drawSession(SessionUnit session) {
		List<DrawResultUnit> results = new ArrayList<>();
		Long sessionId = session.getSessionId();
		int currentOrder = 1;

		// 获取所有个人项目和团队项目
		List<SessionItemVo> individualProjects = session.getItemList().stream()
				.filter(p -> Objects.equals(p.getItemType(), ItemTypeEnum.TYPE_SINGLE.getType()))
				.collect(Collectors.toList());

		List<SessionItemVo> teamProjects = session.getItemList().stream()
				.filter(p -> Objects.equals(p.getItemType(), ItemTypeEnum.TYPE_TEAM.getType()))
				.collect(Collectors.toList());
		GameSessionSettingEntity settingEntity = sessionManager.getSettingBySessionId(session.getGameId(), sessionId);
		if (Objects.isNull(settingEntity)) {
			settingEntity = new GameSessionSettingEntity();
		}

		// 获取间隔范围
		int orgRangeMin = settingEntity.getOrgMin();
		int orgRangeMax = settingEntity.getOrgMax();
		int teamRangeMin = settingEntity.getTeamMin();
		int teamRangeMax = settingEntity.getTeamMax();
		int singleRangeMin = settingEntity.getSingleMin();
		int singleRangeMax = settingEntity.getSingleMax();

		// 交替处理个人项目和团队项目
		Iterator<SessionItemVo> individualIterator = individualProjects.iterator();
		Iterator<SessionItemVo> teamIterator = teamProjects.iterator();

		while (individualIterator.hasNext() || teamIterator.hasNext()) {
			if (individualIterator.hasNext()) {
				SessionItemVo project = individualIterator.next();
				List<SignSingleDto> players = project.getSingleList();
				List<DrawResultUnit> projectResults = drawIndividualProject(project, players, currentOrder, singleRangeMin, singleRangeMax, teamRangeMin, teamRangeMax, orgRangeMin, orgRangeMax);
				results.addAll(projectResults);
				currentOrder += projectResults.size();
			}

			if (teamIterator.hasNext()) {
				SessionItemVo project = teamIterator.next();
				List<SignTeamDto> teams = project.getTeamList();
				List<DrawResultUnit> projectResults = drawTeamProject(project, teams, currentOrder, teamRangeMin, teamRangeMax, orgRangeMin, orgRangeMax);
				results.addAll(projectResults);
				currentOrder += projectResults.size();
			}
		}

		return results;
	}

	private List<DrawResultUnit> drawIndividualProject(SessionItemVo project, List<SignSingleDto> players, int startOrder, int singleRangeMin, int singleRangeMax, int teamRangeMin, int teamRangeMax, int orgRangeMin, int orgRangeMax) {
		List<DrawResultUnit> results = new ArrayList<>();
		List<SignSingleDto> remainingPlayers = new ArrayList<>(players);
		int currentOrder = startOrder;

		Map<Long, Integer> orgLastIndex = new HashMap<>(); // 记录每个组织最后出现的索引
		Map<Long, Integer> playerLastIndex = new HashMap<>(); // 记录每个选手最后出现的索引

		// 1. 随机打乱参与人员
		Collections.shuffle(remainingPlayers, new Random());

		while (!remainingPlayers.isEmpty()) {
			SignSingleDto selectedPlayer = findValidPlayer(remainingPlayers, results, orgLastIndex, playerLastIndex, singleRangeMin, singleRangeMax, teamRangeMin, teamRangeMax, orgRangeMin, orgRangeMax);
			if (selectedPlayer == null) {
				// 如果没有找到符合条件的选手，说明无法满足所有间隔要求
				// 这种情况下，我们选择第一个未排序的选手
				selectedPlayer = remainingPlayers.get(0);
			}

			DrawResultUnit result = new DrawResultUnit();
			result.setProject(project);
			result.setPlayer(selectedPlayer);
			result.setOrder(currentOrder++);
			results.add(result);

			remainingPlayers.remove(selectedPlayer);
			orgLastIndex.put(selectedPlayer.getOrgId(), currentOrder - 1);
			playerLastIndex.put(selectedPlayer.getApplyId(), currentOrder - 1);
		}

		return results;
	}

	private List<DrawResultUnit> drawTeamProject(SessionItemVo project, List<SignTeamDto> teams, int startOrder, int teamRangeMin, int teamRangeMax, int orgRangeMin, int orgRangeMax) {
		List<DrawResultUnit> results = new ArrayList<>();
		List<SignTeamDto> remainingTeams = new ArrayList<>(teams);
		int currentOrder = startOrder;

		Map<Long, Integer> orgLastIndex = new HashMap<>(); // 记录每个组织最后出现的索引
		Map<Long, Integer> teamLastIndex = new HashMap<>(); // 记录每个队伍最后出现的索引

		// 1. 随机打乱参与队伍
		Collections.shuffle(remainingTeams, new Random());

		while (!remainingTeams.isEmpty()) {
			SignTeamDto selectedTeam = findValidTeam(remainingTeams, results, orgLastIndex, teamLastIndex, teamRangeMin, teamRangeMax, orgRangeMin, orgRangeMax);
			if (selectedTeam == null) {
				// 如果没有找到符合条件的队伍，说明无法满足所有间隔要求
				// 这种情况下，我们选择第一个未排序的队伍
				selectedTeam = remainingTeams.get(0);
			}

			DrawResultUnit result = new DrawResultUnit();
			result.setProject(project);
			result.setTeam(selectedTeam);
			result.setOrder(currentOrder++);
			results.add(result);

			remainingTeams.remove(selectedTeam);
			orgLastIndex.put(selectedTeam.getOrgId(), currentOrder - 1);
			teamLastIndex.put(selectedTeam.getTeamId(), currentOrder - 1);
		}

		return results;
	}

	private SignSingleDto findValidPlayer(List<SignSingleDto> remainingPlayers, List<DrawResultUnit> existingResults, Map<Long, Integer> orgLastIndex, Map<Long, Integer> playerLastIndex, int singleRangeMin, int singleRangeMax, int teamRangeMin, int teamRangeMax, int orgRangeMin, int orgRangeMax) {
		for (SignSingleDto player : remainingPlayers) {
			if (isValidPlayerPlacement(player, existingResults, orgLastIndex, playerLastIndex, singleRangeMin, singleRangeMax, teamRangeMin, teamRangeMax, orgRangeMin, orgRangeMax)) {
				return player;
			}
		}
		return null;
	}

	private SignTeamDto findValidTeam(List<SignTeamDto> remainingTeams, List<DrawResultUnit> existingResults, Map<Long, Integer> orgLastIndex, Map<Long, Integer> teamLastIndex, int teamRangeMin, int teamRangeMax, int orgRangeMin, int orgRangeMax) {
		for (SignTeamDto team : remainingTeams) {
			if (isValidTeamPlacement(team, existingResults, orgLastIndex, teamLastIndex, teamRangeMin, teamRangeMax, orgRangeMin, orgRangeMax)) {
				return team;
			}
		}
		return null;
	}

	private boolean isValidPlayerPlacement(SignSingleDto player, List<DrawResultUnit> existingResults, Map<Long, Integer> orgLastIndex, Map<Long, Integer> playerLastIndex, int singleRangeMin, int singleRangeMax, int teamRangeMin, int teamRangeMax, int orgRangeMin, int orgRangeMax) {
		if (existingResults.isEmpty()) {
			return true;
		}

		int lastIndex = existingResults.size() - 1;

		// 检查组织间隔
		if (orgLastIndex.containsKey(player.getOrgId())) {
			int lastOrgIndex = orgLastIndex.get(player.getOrgId());
			if (lastIndex - lastOrgIndex < orgRangeMin || lastIndex - lastOrgIndex > orgRangeMax) {
				return false;
			}
		}

		// 检查选手间隔
		if (playerLastIndex.containsKey(player.getApplyId())) {
			int lastPlayerIndex = playerLastIndex.get(player.getApplyId());
			if (lastIndex - lastPlayerIndex < singleRangeMin || lastIndex - lastPlayerIndex > singleRangeMax) {
				return false;
			}
		}

		return true;
	}

	private boolean isValidTeamPlacement(SignTeamDto team, List<DrawResultUnit> existingResults, Map<Long, Integer> orgLastIndex, Map<Long, Integer> teamLastIndex, int teamRangeMin, int teamRangeMax, int orgRangeMin, int orgRangeMax) {
		if (existingResults.isEmpty()) {
			return true;
		}

		int lastIndex = existingResults.size() - 1;

		// 检查组织间隔
		if (orgLastIndex.containsKey(team.getOrgId())) {
			int lastOrgIndex = orgLastIndex.get(team.getOrgId());
			if (lastIndex - lastOrgIndex < orgRangeMin || lastIndex - lastOrgIndex > orgRangeMax) {
				return false;
			}
		}

		// 检查队伍间隔
		if (teamLastIndex.containsKey(team.getTeamId())) {
			int lastTeamIndex = teamLastIndex.get(team.getTeamId());
			if (lastIndex - lastTeamIndex < teamRangeMin || lastIndex - lastTeamIndex > teamRangeMax) {
				return false;
			}
		}

		return true;
	}
}

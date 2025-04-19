package com.jsnjwj.facade.service.v2.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.dao.SessionDrawListDao;
import com.jsnjwj.facade.dto.AreaSessionDto;
import com.jsnjwj.facade.dto.ArrangeAreaSessionDto;
import com.jsnjwj.facade.dto.ArrangeSessionVo;
import com.jsnjwj.facade.dto.SessionChooseDto;
import com.jsnjwj.facade.entity.*;
import com.jsnjwj.facade.manager.*;
import com.jsnjwj.facade.mapper.ArrangeAreaSessionMapper;
import com.jsnjwj.facade.mapper.GameAreaMapper;
import com.jsnjwj.facade.mapper.GameSessionMapper;
import com.jsnjwj.facade.query.GameGroupingAreaSetQuery;
import com.jsnjwj.facade.query.GameGroupingSetNumQuery;
import com.jsnjwj.facade.query.ManualDrawAreaSessionBatchQuery;
import com.jsnjwj.facade.service.v2.ArrangeAreaService;
import com.jsnjwj.facade.vo.AreaSessionVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 场地安排arrange/area
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ArrangeAreaServiceImpl implements ArrangeAreaService {

	private final GameGroupingManager gameGroupingManager;

	private final GameAreaManager gameAreaManager;

	private final ArrangeAreaSessionManager arrangeAreaSessionManager;

	private final ArrangeSessionItemManager arrangeSessionItemManager;

	private final ArrangeAreaSessionMapper arrangeAreaSessionMapper;

	private final GameSessionMapper gameSessionMapper;

	private final GameAreaMapper gameAreaMapper;

	@Override
	public ApiResponse<?> setAreaNum(GameGroupingSetNumQuery query) {
		if (query.getAreaNum() <= 0)
			return ApiResponse.error("请输入正确的场地数");
		int courtNum = 1;
		gameGroupingManager.resetCourt(query.getGameId());

		List<GameAreaEntity> areas = new ArrayList<>();
		while (courtNum <= query.getAreaNum()) {
			GameAreaEntity area = new GameAreaEntity();
			area.setGameId(query.getGameId());
			area.setAreaName("场地" + courtNum);
			area.setAreaNo(courtNum);
			area.setStatus(1);
			areas.add(area);
			courtNum++;
		}
		gameGroupingManager.saveCourts(areas);
		return ApiResponse.success(true);
	}

	@Override
	public ApiResponse<Boolean> saveArea(GameGroupingAreaSetQuery query) {
		GameAreaEntity area = new GameAreaEntity();
		area.setId(query.getAreaId());
		area.setGameId(query.getGameId());
		area.setAreaName(query.getAreaName());
		area.setStatus(query.getStatus());
		gameGroupingManager.saveCourt(area);

		if (CollectionUtil.isNotEmpty(query.getSessionIds())) {
			LambdaQueryWrapper<ArrangeAreaSessionEntity> queryWrapper = new LambdaQueryWrapper<>();
			queryWrapper.eq(ArrangeAreaSessionEntity::getAreaId, query.getAreaId());
			queryWrapper.eq(ArrangeAreaSessionEntity::getGameId, query.getGameId());
			arrangeAreaSessionMapper.delete(queryWrapper);

			for (Long sessionId : query.getSessionIds()) {
				ArrangeAreaSessionEntity session = new ArrangeAreaSessionEntity();
				session.setSessionId(sessionId);
				session.setGameId(query.getGameId());
				session.setGameId(query.getGameId());
				session.setAreaId(query.getAreaId());
				session.setAreaNo(query.getAreaNo());
				session.setCreatedAt(new Date());
				arrangeAreaSessionMapper.insert(session);
			}
		}

		return ApiResponse.success(true);
	}

	@Override
	public ApiResponse<List<ArrangeAreaSessionDto>> getAreas(Long gameId) {
		List<GameAreaEntity> areas = gameGroupingManager.getCourts(gameId);

		List<ArrangeAreaSessionDto> response = areas.stream().map(area -> {

			ArrangeAreaSessionDto sessionVo = new ArrangeAreaSessionDto();
			sessionVo.setAreaId(area.getId());
			sessionVo.setAreaName(area.getAreaName());
			sessionVo.setAreaNo(area.getAreaNo());
			List<ArrangeSessionVo> sessionVoList = gameAreaManager.selectSession(gameId, area.getId());
			sessionVo.setSessionList(sessionVoList);
			return sessionVo;
		}).collect(Collectors.toList());

		return ApiResponse.success(response);
	}

	/**
	 * 查询可排场次列表
	 * @param query
	 * @return
	 */
	@Override
	public ApiResponse<List<AreaSessionDto>> selectSessionList(GameGroupingAreaSetQuery query) {
		// 1.获取已设置的场次(不包含默认场次)
		List<AreaSessionDto> response = new ArrayList<>();
		// 2.获取所有场地
		List<GameAreaEntity> areaEntities = gameGroupingManager.getCourts(query.getGameId());
		if (CollectionUtil.isEmpty(areaEntities)){
			return ApiResponse.success(response);
		}

		response = areaEntities.stream().map(item->{

			AreaSessionDto areaSessionDto = new AreaSessionDto();
			areaSessionDto.setAreaName(item.getAreaName());
			areaSessionDto.setAreaId(item.getId());
			// 3.查询每个场地已排场次
			List<AreaSessionVo> arrangeAreaSessionEntities = arrangeAreaSessionManager.getSessionByAreaId(query.getGameId(),item.getId());
			if (CollectionUtil.isNotEmpty(arrangeAreaSessionEntities)){
				areaSessionDto.setData(arrangeAreaSessionEntities);
			}

			return areaSessionDto;
		}).collect(Collectors.toList());



		return ApiResponse.success(response);
	}

	@Override
	public ApiResponse<AreaSessionDto> selectUnSessionList(GameGroupingAreaSetQuery query) {
		AreaSessionDto result = new AreaSessionDto();

		result.setGameId(query.getGameId());
		result.setAreaName("未排场次");

		// 查询已排场地场次

		List<ArrangeAreaSessionEntity> arrangedSessionIds = arrangeAreaSessionManager.selectArrangedSession(query.getGameId());

		LambdaQueryWrapper<GameSessionEntity> listQuery = new LambdaQueryWrapper<>();
		listQuery.eq(GameSessionEntity::getGameId, query.getGameId());
		if (CollectionUtil.isNotEmpty(arrangedSessionIds)){
			listQuery.notIn(GameSessionEntity::getId, arrangedSessionIds.stream().map(ArrangeAreaSessionEntity::getSessionId).collect(Collectors.toList()));
		}
		List<GameSessionEntity> list = gameSessionMapper.selectList(listQuery);
		result.setData(list.stream().map(item->{
			AreaSessionVo areaSessionVo = new AreaSessionVo();
			areaSessionVo.setSessionId(item.getId());
			areaSessionVo.setSessionName(item.getSessionName());
			return areaSessionVo;
		}).collect(Collectors.toList()));
		return ApiResponse.success(result);

	}

	/**
	 * 场地-场次 随机分组
	 * @param query
	 * @return
	 */
	@Override
	public ApiResponse<Boolean> arrangeSessionRandom(ManualDrawAreaSessionBatchQuery query) {
		Long gameId = query.getGameId();
		List<GameAreaEntity> areaEntities = gameAreaMapper.selectList(
				new LambdaQueryWrapper<GameAreaEntity>().eq(GameAreaEntity::getGameId, query.getGameId()));
		if (CollectionUtil.isEmpty(areaEntities)) {
			return ApiResponse.error("请先设置场地");
		}
		// 清空所有场次项目
		arrangeAreaSessionManager.deleteBySessionId(query.getGameId(), null);
		LambdaQueryWrapper<GameSessionEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(GameSessionEntity::getGameId, gameId);
		List<GameSessionEntity> sessionList = gameSessionMapper.selectList(queryWrapper);
		if (CollectionUtil.isEmpty(sessionList)) {
			return ApiResponse.error("请先设置场次");
		}

		Map<Long, List<GameSessionEntity>> allocateResult = allocateAllProjects(sessionList, areaEntities);
		log.info("分配结果：{}", JSON.toJSONString(allocateResult));
		if (Objects.isNull(allocateResult)) {
			return ApiResponse.error("随机分配失败");
		}
		List<ArrangeAreaSessionEntity> result = new ArrayList<>();
		allocateResult.forEach((areaId, itemEntities) -> {
			AtomicInteger sort = new AtomicInteger();
			itemEntities.forEach(item -> {
				sort.getAndIncrement();
				ArrangeAreaSessionEntity gameSessionItemEntity = new ArrangeAreaSessionEntity();
				gameSessionItemEntity.setGameId(query.getGameId());
				gameSessionItemEntity.setAreaId(areaId);
				gameSessionItemEntity.setSessionId(item.getId());
				gameSessionItemEntity.setSort(sort.get());
				gameSessionItemEntity.setCreatedAt(new Date());
				result.add(gameSessionItemEntity);
			});
		});
		arrangeAreaSessionMapper.saveBatch(result);

		return ApiResponse.success(true);
	}

	@Override
	public ApiResponse<Boolean> arrangeSessionSave(ManualDrawAreaSessionBatchQuery query) {
		Long gameId = query.getGameId();
		List<ArrangeAreaSessionEntity> result = new ArrayList<>();
		if (CollectionUtil.isNotEmpty(query.getData())) {
			query.getData().forEach(session -> {
				Long areaId = session.getAreaId();
				arrangeAreaSessionManager.deleteByAreaId(gameId, areaId);

				session.getData().forEach(item -> {
					ArrangeAreaSessionEntity gameSessionItemEntity = new ArrangeAreaSessionEntity();
					gameSessionItemEntity.setGameId(gameId);
					gameSessionItemEntity.setAreaId(areaId);
					gameSessionItemEntity.setSessionId(item.getSessionId());
					gameSessionItemEntity.setSort(item.getSort());
					gameSessionItemEntity.setCreatedAt(new Date());
					result.add(gameSessionItemEntity);
				});
			});
			if (CollectionUtil.isNotEmpty(result)) {
				arrangeAreaSessionMapper.saveBatch(result);
			}
		}

		return ApiResponse.success(true);
	}

	public Map<Long, List<GameSessionEntity>> allocateAllProjects(List<GameSessionEntity> sessions,
															   List<GameAreaEntity> areas) {
		// 验证输入参数
		if (areas == null || areas.isEmpty()) {
			throw new IllegalArgumentException("场次列表不能为空");
		}
		if (sessions == null || sessions.isEmpty()) {
			throw new IllegalArgumentException("项目列表不能为空");
		}

		// 创建sessionId到项目列表的映射
		Map<Long, List<GameSessionEntity>> allocation = new HashMap<>();
		for (GameAreaEntity session : areas) {
			allocation.put(session.getId(), new ArrayList<>());
		}

		// 复制项目列表并随机打乱
		List<GameSessionEntity> shuffledProjects = new ArrayList<>(sessions);
		Collections.shuffle(shuffledProjects, new Random());

		// 计算基本每个场次应该分配的项目数量和剩余项目数
		int sessionCount = areas.size();
		int baseProjectsPerSession = shuffledProjects.size() / sessionCount;
		int remainingProjects = shuffledProjects.size() % sessionCount;

		// 分配项目到场次
		int projectIndex = 0;
		for (GameAreaEntity session : areas) {
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

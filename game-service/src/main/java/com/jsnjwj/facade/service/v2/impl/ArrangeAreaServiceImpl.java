package com.jsnjwj.facade.service.v2.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.dao.SessionDrawListDao;
import com.jsnjwj.facade.dto.ArrangeAreaSessionDto;
import com.jsnjwj.facade.dto.ArrangeSessionVo;
import com.jsnjwj.facade.dto.SessionChooseDto;
import com.jsnjwj.facade.entity.ArrangeAreaSessionEntity;
import com.jsnjwj.facade.entity.GameAreaEntity;
import com.jsnjwj.facade.entity.GameSessionEntity;
import com.jsnjwj.facade.manager.*;
import com.jsnjwj.facade.mapper.ArrangeAreaSessionMapper;
import com.jsnjwj.facade.query.GameGroupingAreaSetQuery;
import com.jsnjwj.facade.query.GameGroupingSetNumQuery;
import com.jsnjwj.facade.service.v2.ArrangeAreaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
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
	private final ArrangeSessionManager arrangeSessionManager;
	private final ArrangeSessionItemManager arrangeSessionItemManager;
	private final ArrangeAreaSessionMapper arrangeAreaSessionMapper;
	private final ArrangeDrawManager arrangeDrawManager;

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

		if (CollectionUtil.isNotEmpty(query.getSessionIds())){
			LambdaQueryWrapper<ArrangeAreaSessionEntity> queryWrapper = new LambdaQueryWrapper<>();
			queryWrapper.eq(ArrangeAreaSessionEntity::getAreaId, query.getAreaId());
			queryWrapper.eq(ArrangeAreaSessionEntity::getGameId, query.getGameId());
			arrangeAreaSessionMapper.delete(queryWrapper);

			for (Long sessionId : query.getSessionIds()){
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
	public ApiResponse<List<SessionChooseDto>> selectSessionList(GameGroupingAreaSetQuery query) {
		// 1.获取已设置的场次(不包含默认场次)
		List<GameSessionEntity> gameSessionEntities = arrangeSessionManager.getList(query.getGameId());
		Map<Long, GameSessionEntity> gameSessionEntityMap = gameSessionEntities.stream()
				.collect(Collectors.toMap(GameSessionEntity::getId, session -> session));

		List<Long> sessionIds = new ArrayList<>();

		if (CollectionUtil.isNotEmpty(gameSessionEntities)){
			List<Long> chosenSessionIds = gameSessionEntities.stream()
					.filter(Objects::nonNull)
					.map(GameSessionEntity::getId)
					.collect(Collectors.toList());
			sessionIds.addAll(chosenSessionIds);
		}

		// 2.获取已分组过的场次（包含默认场次）
		List<SessionDrawListDao> sessionVoList = arrangeDrawManager.getSessionList(query.getGameId());
		if (CollectionUtil.isNotEmpty(sessionVoList)){
			sessionIds.addAll(sessionVoList.stream().map(SessionDrawListDao::getSessionId).collect(Collectors.toList()));
		}

		List<SessionChooseDto> response = new ArrayList<>();
		if (CollectionUtil.isEmpty(sessionIds)){
			return ApiResponse.success(response);
		}

		// 查询当前场地已排场次
		List<ArrangeSessionVo> selectedSession = gameAreaManager.selectSessionExceptArea(query.getGameId(), query.getAreaId());
		Map<Long, ArrangeSessionVo> selectedSessionMap = selectedSession.stream()
				.collect(Collectors.toMap(ArrangeSessionVo::getSessionId, session -> session));


		response = sessionIds.stream().distinct().map(session -> {
			SessionChooseDto dto = new SessionChooseDto();
			dto.setAreaId(query.getAreaId());

			if (session == 0L){
				dto.setSessionName("默认场次");
				dto.setSessionId(0L);
			}else{
				dto.setSessionId(session);
				dto.setSessionName(gameSessionEntityMap.get(session).getSessionName());
			}
//			dto.setDisabled(true);
//
			if (selectedSessionMap.containsKey(session)){
				dto.setDisabled(true);
			}else{
				dto.setDisabled(false);
			}
			return dto;
		}).collect(Collectors.toList());
		return ApiResponse.success(response);
	}


}

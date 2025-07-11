package com.jsnjwj.facade.service.v2.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson2.JSON;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.dto.ArrangeSessionInfoDto;
import com.jsnjwj.facade.dto.ArrangeSessionVo;
import com.jsnjwj.facade.entity.*;
import com.jsnjwj.facade.manager.ArrangeAreaSessionManager;
import com.jsnjwj.facade.manager.ArrangeSessionItemManager;
import com.jsnjwj.facade.manager.ArrangeSessionManager;
import com.jsnjwj.facade.manager.GameItemManager;
import com.jsnjwj.facade.query.session.GameGroupingSessionSetNumQuery;
import com.jsnjwj.facade.query.session.GameGroupingSessionSetQuery;
import com.jsnjwj.facade.service.v2.ArrangeSessionService;
import com.jsnjwj.facade.vo.session.SessionItemVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 场地安排
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ArrangeSessionServiceImpl implements ArrangeSessionService {

	private final ArrangeSessionManager arrangeSessionManager;

	private final ArrangeSessionItemManager arrangeSessionItemManager;

	private final ArrangeAreaSessionManager arrangeAreaSessionManager;

	private final GameItemManager gameItemManager;

	/**
	 * 创建场地
	 */
	@Override
	public ApiResponse<?> setSessionNum(GameGroupingSessionSetNumQuery query) {
		if (query.getSessionNum() <= 0)
			return ApiResponse.error("请输入正确的场地数");
		int courtNum = 1;
		// 重置所有场次数据
		arrangeSessionManager.resetCourt(query.getGameId());
		// 重置场次项目编排数据
		if (arrangeSessionItemManager.checkSessionItemExist(query.getGameId(), null)) {
			arrangeSessionItemManager.deleteBySessionId(query.getGameId(), null);
		}
		// 重置场地-场次编排数据
		if (arrangeAreaSessionManager.checkSessionItemExist(query.getGameId(), null)) {
			arrangeAreaSessionManager.deleteBySessionId(query.getGameId(), null);
		}
		List<GameSessionEntity> areas = new ArrayList<>();
		while (courtNum <= query.getSessionNum()) {
			GameSessionEntity area = new GameSessionEntity();
			area.setGameId(query.getGameId());
			area.setSessionName("场次" + courtNum);
			area.setSessionNo(courtNum);
			area.setStatus(1);
			areas.add(area);
			courtNum++;
		}
		arrangeSessionManager.saveSessionBatch(areas);
		return ApiResponse.success(true);
	}

	/**
	 * 新增单个场次
	 * @param gameId
	 * @return
	 */
	@Override
	public ApiResponse<?> addSession(Long gameId) {
		List<GameSessionEntity> sessionEntities = arrangeSessionManager.getListByGameId(gameId);
		int courtNum = 1;
		GameSessionEntity areaEntity = new GameSessionEntity();
		if (CollectionUtil.isEmpty(sessionEntities)) {
			areaEntity.setGameId(gameId);
			areaEntity.setSessionName("场次" + courtNum);
			areaEntity.setSessionNo(courtNum);
			areaEntity.setStatus(1);
		}
		else {
			courtNum = sessionEntities.get(sessionEntities.size() - 1).getSessionNo() + 1;
			areaEntity.setGameId(gameId);
			areaEntity.setSessionName("场次" + courtNum);
			areaEntity.setSessionNo(courtNum);
			areaEntity.setStatus(1);
		}
		arrangeSessionManager.saveSession(areaEntity);

		return ApiResponse.success();
	}

	/**
	 * 删除单个场次
	 * @param query
	 * @return
	 */
	@Override
	public ApiResponse<?> deleteSession(GameGroupingSessionSetQuery query) {
		Long sessionId = query.getSessionId();
		Long gameId = query.getGameId();
		// 校验该场次下，是否存在已排项目
		List<GameSessionItemEntity> gameSessionItemEntities = arrangeSessionItemManager.fetchListBySessionId(gameId,
				sessionId);
		if (CollectionUtil.isNotEmpty(gameSessionItemEntities)) {
			arrangeSessionItemManager.deleteBySessionId(gameId, sessionId);
		}
		// 删除场次
		arrangeSessionManager.deleteById(gameId, sessionId);

		return ApiResponse.success();
	}

	/**
	 * 保存场地
	 * @param query
	 * @return
	 */
	@Override
	public ApiResponse<Boolean> saveSession(GameGroupingSessionSetQuery query) {
		Long gameId = query.getGameId();
		Long sessionId = query.getSessionId();
		if (Objects.isNull(sessionId)) {
			return ApiResponse.error("该场次不存在");
		}
		GameSessionEntity session = arrangeSessionManager.getBySessionId(sessionId);
		if (Objects.isNull(session)) {
			return ApiResponse.error("该场次不存在");
		}

		session.setSessionName(query.getSessionName());
		session.setSessionNo(query.getSessionNo());
		session.setStatus(query.getStatus());
		session.setCreatedAt(new Date());
		session.setUpdatedAt(new Date());
		arrangeSessionManager.saveSession(session);

		GameSessionSettingEntity settingEntity = arrangeSessionManager.getSettingBySessionId(gameId, sessionId);
		if (Objects.isNull(settingEntity)) {
			settingEntity = new GameSessionSettingEntity();
			settingEntity.setSessionId(sessionId);
			settingEntity.setGameId(gameId);
			settingEntity.setCreatedAt(new Date());
		}
		GameGroupingSessionSetQuery.SessionSettingVo settingVo = query.getSessionSetting();
		settingEntity.setOrgMin(settingVo.getOrgRange().stream().sorted().collect(Collectors.toList()).get(0));
		settingEntity.setOrgMax(settingVo.getOrgRange().stream().sorted().collect(Collectors.toList()).get(1));
		settingEntity.setTeamMin(settingVo.getTeamRange().stream().sorted().collect(Collectors.toList()).get(0));
		settingEntity.setTeamMax(settingVo.getTeamRange().stream().sorted().collect(Collectors.toList()).get(1));
		settingEntity.setSingleMin(settingVo.getSingleRange().stream().sorted().collect(Collectors.toList()).get(0));
		settingEntity.setSingleMax(settingVo.getSingleRange().stream().sorted().collect(Collectors.toList()).get(1));
		arrangeSessionManager.saveSessionSetting(settingEntity);

		return ApiResponse.success(true);
	}

	/**
	 * 获取所有场地对应场次信息
	 * @param gameId
	 * @return
	 */
	@Override
	public ApiResponse<List<ArrangeSessionVo>> getSessions(Long gameId) {
		// 获取所有场次信息
		List<GameSessionEntity> sessionList = arrangeSessionManager.getListByGameId(gameId);

		if (CollectionUtil.isEmpty(sessionList)) {
			return ApiResponse.success(new ArrayList<>());
		}
		List<ArrangeSessionVo> response;

		response = sessionList.stream().map(session -> {
			ArrangeSessionVo arrangeSessionVo = new ArrangeSessionVo();
			arrangeSessionVo.setSessionId(session.getId());
			arrangeSessionVo.setSessionName(session.getSessionName());
			arrangeSessionVo.setSessionNo(session.getSessionNo());

			// 校验该场次下，是否存在已排项目
			List<SessionItemVo> sessionItemList = arrangeSessionItemManager.fetchBySessionId(gameId, session.getId());
			if (CollectionUtil.isNotEmpty(sessionItemList)) {

				arrangeSessionVo.setItemNum(sessionItemList.size());

				arrangeSessionVo.setItemList(sessionItemList);

			}

			GameSessionSettingEntity settingEntity = arrangeSessionManager.getSettingBySessionId(gameId,
					session.getId());
			if (Objects.nonNull(settingEntity)) {
				ArrangeSessionVo.SessionSettingVo settingVo = new ArrangeSessionVo.SessionSettingVo();
				settingVo.setOrgRange(Arrays.asList(settingEntity.getOrgMin(), settingEntity.getOrgMax()));
				settingVo.setTeamRange(Arrays.asList(settingEntity.getTeamMin(), settingEntity.getTeamMax()));
				settingVo.setSingleRange(Arrays.asList(settingEntity.getSingleMin(), settingEntity.getSingleMax()));
				arrangeSessionVo.setSessionSetting(settingVo);
			}
			return arrangeSessionVo;
		}).collect(Collectors.toList());

		return ApiResponse.success(response);
	}

	@Override
	public ApiResponse<ArrangeSessionInfoDto> getSessionInfo(Long gameId) {
		Long totalNum = gameItemManager.countByGameId(gameId);
		Long arrangedNum = arrangeSessionItemManager.countByGameId(gameId);
		ArrangeSessionInfoDto arrangeSessionInfoDto = new ArrangeSessionInfoDto();
		arrangeSessionInfoDto.setTotalItemCount(totalNum.intValue());
		arrangeSessionInfoDto.setArrangedItemCount(arrangedNum.intValue());
		return ApiResponse.success(arrangeSessionInfoDto);
	}

}

package com.jsnjwj.facade.service.v2.impl;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.entity.GameSessionEntity;
import com.jsnjwj.facade.manager.ArrangeAreaSessionManager;
import com.jsnjwj.facade.manager.ArrangeSessionItemManager;
import com.jsnjwj.facade.manager.ArrangeSessionManager;
import com.jsnjwj.facade.query.session.GameGroupingSessionSetNumQuery;
import com.jsnjwj.facade.query.session.GameGroupingSessionSetQuery;
import com.jsnjwj.facade.service.v2.ArrangeSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	 * 保存场地
	 * @param query
	 * @return
	 */
	@Override
	public ApiResponse<Boolean> saveSession(GameGroupingSessionSetQuery query) {
		GameSessionEntity area = new GameSessionEntity();
		area.setId(query.getSessionId());
		area.setGameId(query.getGameId());
		area.setSessionName(query.getSessionName());
		area.setSessionNo(query.getSessionNo());
		area.setStatus(query.getStatus());
		area.setCreatedAt(new Date());
		arrangeSessionManager.saveSession(area);
		return ApiResponse.success(true);
	}

	/**
	 * 获取所有场地对应场次信息
	 * @param gameId
	 * @return
	 */
	@Override
	public ApiResponse<List<GameSessionEntity>> getSessions(Long gameId) {
		List<GameSessionEntity> response = arrangeSessionManager.getList(gameId);
		return ApiResponse.success(response);
	}

}

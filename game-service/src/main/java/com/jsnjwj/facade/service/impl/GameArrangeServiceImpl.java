package com.jsnjwj.facade.service.impl;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.dto.GameAreaDto;
import com.jsnjwj.facade.dto.GameItemDto;
import com.jsnjwj.facade.dto.GroupAreaItemDto;
import com.jsnjwj.facade.entity.*;
import com.jsnjwj.facade.enums.GameStatusEnum;
import com.jsnjwj.facade.exception.BusinessException;
import com.jsnjwj.facade.manager.*;
import com.jsnjwj.facade.query.*;
import com.jsnjwj.facade.service.GameArrangeService;
import com.jsnjwj.facade.service.GameGroupService;
import com.jsnjwj.facade.service.GameItemService;
import com.jsnjwj.facade.vo.GameGroupAreaItemListVo;
import com.jsnjwj.facade.vo.GameRuleSettingVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 合同比对service
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class GameArrangeServiceImpl implements GameArrangeService {

	@Resource
	private GameGroupingManager gameGroupingManager;

	@Resource
	private GameSettingRuleManager gameSettingRuleManager;

	@Resource
	private GameItemService gameItemService;

	@Resource
	private GameGroupService gameGroupService;

	@Resource
	private GameGroupManager groupManager;

	@Resource
	private GameItemManager itemManager;

	@Resource
	private GameAreaManager gameAreaManager;

	@Resource
	private GameManager gameManager;

	@Override
	public ApiResponse<?> setCourtNum(GameGroupingSetNumQuery query) {
		if (query.getAreaNum() <= 0)
			return ApiResponse.error("请输入正确的场地数");
		int courtNum = 1;
		gameGroupingManager.resetCourt(query.getGameId());

		List<TcGameArea> areas = new ArrayList<>();
		while (courtNum <= query.getAreaNum()) {
			TcGameArea area = new TcGameArea();
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
	public ApiResponse<Boolean> saveCourt(GameGroupingAreaSetQuery query) {
		TcGameArea area = new TcGameArea();
		area.setId(query.getAreaId());
		area.setGameId(query.getGameId());
		area.setAreaName(query.getAreaName());
		area.setStatus(query.getStatus());
		gameGroupingManager.saveCourt(area);
		return ApiResponse.success(true);
	}

	@Override
	public ApiResponse<List<TcGameArea>> getCourts(Long gameId) {
		List<TcGameArea> response = gameGroupingManager.getCourts(gameId);
		return ApiResponse.success(response);
	}

	/**
	 * 批量分组
	 * @param query
	 * @return
	 */
	@Override
	public ApiResponse<Boolean> setGroupingBatch(GameGroupingSetQuery query) {
		Long gameId = query.getGameId();
		TcGames game = gameManager.fetchInfo(gameId);

		if (GameStatusEnum.GAME_REGISTRATION_CLOSED.getCode()<=game.getStatus()){
			throw new BusinessException("赛事已开始，无法操作");
		}

		//查询所有场地
		List<TcGameArea> areaList = gameAreaManager.getAvailableCourts(query.getGameId());


		return ApiResponse.success(true);
	}

	/**
	 * 单个分组
	 * @param query
	 * @return
	 */

	@Override
	public ApiResponse<Boolean> setGrouping(GameGroupingSetQuery query) {
		gameGroupingManager.resetGrouping(query);

		List<TcGameAreaItem> areaItems = new ArrayList<>();
		if (!query.getItemIds().isEmpty()) {
			Integer sort = 1;
			for (Long i : query.getItemIds()) {
				TcGameAreaItem item = new TcGameAreaItem();
				item.setGameId(query.getGameId());
				item.setItemId(i);
				item.setAreaNo(query.getAreaNo());
				item.setAreaId(query.getAreaId());
				item.setSort(sort);
				areaItems.add(item);
				sort++;
			}
		}
		gameGroupingManager.saveGroupings(areaItems);

		return ApiResponse.success(true);
	}

	@Override
	public ApiResponse<?> setRules(GameSettingSetRulesQuery query) {
		TcGameRuleSetting ruleSetting = gameSettingRuleManager.fetchRule(query.getGameId(), query.getItemId());

		if (Objects.nonNull(ruleSetting)) {
			ruleSetting.setScoreRule(query.getScoreRule());
			ruleSetting.setJudgeGroupNum(query.getJudgeGroupNum());
			List<TcGameRuleSettingDetail> details = new ArrayList<>();

			for (GameSettingSetRulesQuery.RuleContent ruleContent : query.getRuleContentList()) {
				TcGameRuleSettingDetail detail = new TcGameRuleSettingDetail();
				detail.setScoreWeight(ruleContent.getRuleWeight());
				detail.setExtraName(ruleContent.getRuleExtraName());
				detail.setScoreRatio(ruleContent.getRuleRatio());
				detail.setExtraType(ruleContent.getRuleExtraType());
				detail.setScoreWeight(ruleContent.getRuleWeight());
				detail.setSettingId(ruleSetting.getId());
				details.add(detail);
			}

			gameSettingRuleManager.saveRuleDetail(query.getGameId(), query.getItemId(), details);
			return ApiResponse.success(true);
		}

		ruleSetting = new TcGameRuleSetting();

		ruleSetting.setGameId(query.getGameId());
		ruleSetting.setItemId(query.getItemId());
		ruleSetting.setScoreRule(query.getScoreRule());
		ruleSetting.setJudgeGroupNum(query.getJudgeGroupNum());
		ruleSetting.setCreatedAt(new Date());
		gameSettingRuleManager.saveRuleInfo(ruleSetting);

		List<TcGameRuleSettingDetail> details = new ArrayList<>();

		for (GameSettingSetRulesQuery.RuleContent ruleContent : query.getRuleContentList()) {
			TcGameRuleSettingDetail detail = new TcGameRuleSettingDetail();
			detail.setScoreWeight(ruleContent.getRuleWeight());
			detail.setExtraName(ruleContent.getRuleExtraName());
			detail.setScoreRatio(ruleContent.getRuleRatio());
			detail.setExtraType(ruleContent.getRuleExtraType());
			detail.setScoreWeight(ruleContent.getRuleWeight());
			detail.setSettingId(ruleSetting.getId());
			details.add(detail);
		}

		gameSettingRuleManager.saveRuleDetail(query.getGameId(), query.getItemId(), details);
		return ApiResponse.success(true);
	}

	@Override
	public ApiResponse<?> getRules(Long gameId, Long itemId) {
		TcGameRuleSetting ruleSettingVo = gameSettingRuleManager.fetchRule(gameId, itemId);

		List<TcGameRuleSettingDetail> ruleSettingDetails = gameSettingRuleManager
			.fetchRuleDetail(ruleSettingVo.getId());

		GameRuleSettingVo response = new GameRuleSettingVo();
		response.setGameId(gameId);
		response.setJudgeGroupNum(ruleSettingVo.getJudgeGroupNum());
		response.setScoreRule(ruleSettingVo.getScoreRule());
		response.setItemId(gameId);

		List<GameRuleSettingVo.GameRuleDetailVo> ruleDetailVos = new ArrayList<>();
		for (TcGameRuleSettingDetail detail : ruleSettingDetails) {
			GameRuleSettingVo.GameRuleDetailVo vo = new GameRuleSettingVo.GameRuleDetailVo();

			vo.setSettingId(ruleSettingVo.getId());
			vo.setNum(detail.getNum());
			vo.setExtraType(detail.getExtraType());
			vo.setExtraName(detail.getExtraName());
			vo.setScoreRatio(detail.getScoreRatio());
			vo.setScoreWeight(detail.getScoreWeight());
			ruleDetailVos.add(vo);

		}
		response.setDetailVoList(ruleDetailVos);
		return ApiResponse.success(response);
	}

	@Override
	public ApiResponse<?> fetchArrangeList(GameGroupingListQuery query) {
		Long gameId = query.getGameId();
		GameGroupAreaItemListVo response = new GameGroupAreaItemListVo();
		response.setGameId(gameId);
		// 获取场地列表
		List<TcGameArea> areaList = gameGroupingManager.getAvailableCourts(gameId);
		if (CollectionUtils.isEmpty(areaList)) {
			return ApiResponse.success(response);
		}
		List<GroupAreaItemDto> content = new ArrayList<>();
		for (TcGameArea area : areaList) {
			GroupAreaItemDto dto = new GroupAreaItemDto();
			GameAreaDto areaDto = new GameAreaDto();
			areaDto.setAreaName(area.getAreaName());
			areaDto.setAreaNo(area.getAreaNo());
			areaDto.setAreaId(area.getId());
			dto.setGameArea(areaDto);

			List<GameItemDto> gameItemDtoList = new ArrayList<>();
			List<TcGameAreaItem> itemList = gameGroupingManager.fetchAreaItems(gameId, area.getId());
			if (CollectionUtils.isEmpty(itemList)) {
				continue;
			}
			for (TcGameAreaItem areaItem : itemList) {
				GameItemDto itemDto = new GameItemDto();
				TcGameItem itemInfo = itemManager.fetchItemInfo(areaItem.getItemId());
				TcGameGroup groupInfo = groupManager.fetchOneInfo(itemInfo.getGameId(), itemInfo.getGroupId());
				itemDto.setItemName(itemInfo.getItemName());
				itemDto.setGroupId(itemInfo.getGroupId());
				itemDto.setSort(areaItem.getSort());
				itemDto.setGroupName(groupInfo.getGroupName());
				gameItemDtoList.add(itemDto);
			}
			dto.setItemList(gameItemDtoList);
			content.add(dto);
			response.setContent(content);
		}

		return ApiResponse.success(response);
	}

	@Override
	public ApiResponse<?> batchGroupingItemCourt(Long gameId) {
		return ApiResponse.success();

	}

	@Override
	public ApiResponse<?> getCourtItem(Long gameId, Long itemId) {
		return ApiResponse.success();
	}

}

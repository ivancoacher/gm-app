package com.jsnjwj.facade.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson2.JSON;
import com.jsnjwj.facade.easyexcel.upload.ImportGroupDto;
import com.jsnjwj.facade.easyexcel.upload.ImportItemDto;
import com.jsnjwj.facade.easyexcel.upload.ImportSingleUploadDto;
import com.jsnjwj.facade.easyexcel.upload.ImportTeamDto;
import com.jsnjwj.facade.entity.GameGroupEntity;
import com.jsnjwj.facade.entity.GameItemEntity;
import com.jsnjwj.facade.entity.SignTeamEntity;
import com.jsnjwj.facade.manager.SignApplyManager;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Data
public class SingleImportListener extends AnalysisEventListener<ImportSingleUploadDto> {

	private static final int BATCH_COUNT = 100;

	@Resource
	private SignApplyManager signApplyManager;

	Map<String, ImportGroupDto> groupMap = new HashMap<>();

	Map<String, ImportItemDto> itemMap = new HashMap<>();

	Map<String, ImportTeamDto> teamMap = new HashMap<>();

	private final Long gameId;

	private List<ImportSingleUploadDto> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

	public SingleImportListener(Long gameId, SignApplyManager signApplyManager) {
		this.gameId = gameId;
		this.signApplyManager = signApplyManager;
	}

	@Override
	public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
		log.info("解析到一条头数据：{}, currentRowHolder: {}", headMap.toString(), context.readRowHolder().getRowIndex());
		signApplyManager.cleanSignData(gameId);
	}

	@Override
	public void invoke(ImportSingleUploadDto singleUploadDto, AnalysisContext analysisContext) {
		log.info("解析到一条数据 before:{}", JSON.toJSONString(singleUploadDto));
		getOrCreateGroup(singleUploadDto);
		getOrCreateItem(singleUploadDto);
		getOrCreateTeam(singleUploadDto);
		cachedDataList.add(singleUploadDto);
		log.info("解析到一条数据 after:{}", JSON.toJSONString(singleUploadDto));

		if (cachedDataList.size() >= BATCH_COUNT) {
			// 存储完成清理 list
			signApplyManager.saveSingleBatch(this.gameId, cachedDataList);
			cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

		}
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext analysisContext) {
		signApplyManager.saveSingleBatch(this.gameId, cachedDataList);
	}

	/**
	 * 获取组别信息
	 * @param singleUploadDto ImportSingleUploadDto
	 */
	private void getOrCreateGroup(ImportSingleUploadDto singleUploadDto) {
		String groupName = singleUploadDto.getGroupName();
		Long groupId;
		// 从缓存中获取组别
		ImportGroupDto group = groupMap.get(groupName);
		if (group == null) {
			if (!signApplyManager.checkGroupExist(this.gameId, groupName)) {
				groupId = signApplyManager.saveGroup(this.gameId, groupName);
			}
			else {
				GameGroupEntity groupEntity = signApplyManager.getGroupEntity(gameId, groupName);
				groupId = groupEntity.getId();
			}
			group = new ImportGroupDto();
			group.setGroupId(groupId);
			group.setGroupName(groupName);
			// 将组别保存到缓存中
			groupMap.put(groupName, group);
		}
		else {
			groupId = group.getGroupId();
		}
		singleUploadDto.setGroupId(String.valueOf(groupId));
	}

	/**
	 * 获取项目信息
	 * @param singleUploadDto ImportSingleUploadDto
	 */
	private void getOrCreateItem(ImportSingleUploadDto singleUploadDto) {
		String itemName = singleUploadDto.getItemName();
		Long groupId = singleUploadDto.getGroupId();
		Long itemId;
		// 从缓存中获取组别
		ImportItemDto itemDto = itemMap.getOrDefault(itemName, null);
		if (itemDto == null) {
			if (!signApplyManager.checkItemExist(this.gameId, groupId, itemName)) {
				itemId = signApplyManager.saveItem(this.gameId, groupId, itemName);
			}
			else {
				GameItemEntity itemEntity = signApplyManager.getItemEntity(gameId, groupId, itemName);
				itemId = itemEntity.getId();
			}
			itemDto = new ImportItemDto();
			itemDto.setItemId(itemId);
			itemDto.setItemName(itemName);
			// 将组别保存到缓存中
			itemMap.put(itemName, itemDto);
		}
		else {
			itemId = itemDto.getItemId();
		}
		singleUploadDto.setItemId(String.valueOf(itemId));
	}

	/**
	 * 获取队伍信息
	 * @param singleUploadDto ImportSingleUploadDto
	 */
	private void getOrCreateTeam(ImportSingleUploadDto singleUploadDto) {
		String teamName = singleUploadDto.getTeamName();
		if (Objects.isNull(teamName)) {
			return;
		}
		Long teamId;
		// 从缓存中获取组别
		ImportTeamDto teamDto = teamMap.getOrDefault(teamName, null);
		if (teamDto == null) {
			if (!signApplyManager.checkTeamExist(this.gameId, teamName)) {
				teamId = signApplyManager.saveTeamByImport(this.gameId, singleUploadDto);
			}
			else {
				SignTeamEntity teamEntity = signApplyManager.getTeamEntity(gameId, teamName);
				teamId = teamEntity.getId();
			}
			teamDto = new ImportTeamDto();
			teamDto.setTeamId(teamId);
			teamDto.setTeamName(teamName);
			// 将组别保存到缓存中
			teamMap.put(teamName, teamDto);
		}
		else {
			teamId = teamDto.getTeamId();
		}
		singleUploadDto.setTeamId(String.valueOf(teamId));

	}

}

package com.jsnjwj.facade.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson2.JSON;
import com.jsnjwj.facade.easyexcel.upload.*;
import com.jsnjwj.facade.entity.GameGroupEntity;
import com.jsnjwj.facade.entity.GameItemEntity;
import com.jsnjwj.facade.entity.SignOrgEntity;
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

	Map<String, ImportOrgDto> orgMap = new HashMap<>();

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
		getOrCreateOrg(singleUploadDto);
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
	 * 获取单位信息
	 * @param singleUploadDto
	 */
	private void getOrCreateOrg(ImportSingleUploadDto singleUploadDto) {
		String orgName = singleUploadDto.getOrgName();
		Long orgId;
		// 从缓存中获取组别
		ImportOrgDto org = orgMap.get(orgName);
		if (org == null) {
			if (!signApplyManager.checkOrgExist(this.gameId, orgName)) {
				orgId = signApplyManager.saveOrg(this.gameId, orgName);
			}
			else {
				SignOrgEntity orgEntity = signApplyManager.getOrgEntity(gameId, orgName);
				orgId = orgEntity.getId();
			}
			org = new ImportOrgDto();
			org.setOrgId(orgId);
			org.setOrgName(orgName);
			// 将组别保存到缓存中
			orgMap.put(orgName, org);
		}
		else {
			orgId = org.getOrgId();
		}
		singleUploadDto.setOrgId(String.valueOf(orgId));
	}

	/**
	 * 获取组别信息
	 * @param singleUploadDto ImportSingleUploadDto
	 */
	private void getOrCreateGroup(ImportSingleUploadDto singleUploadDto) {
		String groupName = singleUploadDto.getGroupName();
		Long groupId;
		// 从缓存中获取组别
		ImportGroupDto group = groupMap.getOrDefault(groupName,null);
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
		String groupName = singleUploadDto.getGroupName();
		Long groupId = singleUploadDto.getGroupId();
		Long itemId;
		// 从缓存中获取组别
		ImportItemDto itemDto = itemMap.getOrDefault(groupName + itemName, null);
		if (itemDto == null) {
			if (!signApplyManager.checkItemExist(this.gameId, groupId, itemName)) {
				itemId = signApplyManager.saveItem(this.gameId, groupId, itemName, singleUploadDto.getItemType());
			}
			else {
				GameItemEntity itemEntity = signApplyManager.getItemEntity(gameId, groupId, itemName);
				itemId = itemEntity.getId();
			}
			itemDto = new ImportItemDto();
			itemDto.setItemId(itemId);
			itemDto.setItemName(itemName);
			// 将组别保存到缓存中
			itemMap.put(groupName + itemName, itemDto);
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
		Long orgId = singleUploadDto.getOrgId();
		Long groupId = singleUploadDto.getGroupId();
		Long itemId = singleUploadDto.getItemId();
		// 从缓存中获取组别
		ImportTeamDto teamDto = teamMap.getOrDefault(teamName, null);
		if (teamDto == null || !Objects.equals(teamDto.getOrgId(), orgId)) {
			if (!signApplyManager.checkTeamExist(this.gameId, orgId, teamName)) {
				teamId = signApplyManager.saveTeamByImport(this.gameId, singleUploadDto);
			}
			else {
				SignTeamEntity teamEntity = signApplyManager.getTeamEntity(gameId, orgId,teamName);

				teamId = teamEntity.getId();
			}
			teamDto = new ImportTeamDto();
			teamDto.setTeamId(teamId);
			teamDto.setOrgId(singleUploadDto.getOrgId());
			teamDto.setGroupId(singleUploadDto.getGroupId());
			teamDto.setItemId(singleUploadDto.getItemId());
			teamDto.setTeamName(teamName);
			teamDto.setCoachName(singleUploadDto.getCoachName());
			teamDto.setLeaderName(singleUploadDto.getLeaderName());
			// 将组别保存到缓存中
			teamMap.put(teamName, teamDto);
		}
		else {
			teamId = teamDto.getTeamId();
			boolean isUpdate = false;
			if (StringUtils.isNotBlank(singleUploadDto.getCoachPhone())) {

				Set<String> coachSet = new HashSet<>(Arrays.asList(teamDto.getCoachName().split(" ")));
				if (!coachSet.contains(singleUploadDto.getCoachName().trim())) {
					isUpdate = true;
					String coach = singleUploadDto.getCoachName().replace(",", " ");

					coachSet.addAll(Arrays.asList(coach.split(" ")));
					teamDto.setCoachName(String.join(" ", new ArrayList<>(coachSet)));
				}
			}
			if (StringUtils.isNotBlank(singleUploadDto.getLeaderName())) {

				Set<String> leaderSet = new HashSet<>(Arrays.asList(teamDto.getLeaderName().split(" ")));
				if (!leaderSet.contains(singleUploadDto.getLeaderName().trim())) {
					isUpdate = true;
					String team = singleUploadDto.getLeaderName().replace(",", " ");
					leaderSet.addAll(Arrays.asList(team.split(" ")));
					teamDto.setLeaderName(String.join(" ", new ArrayList<>(leaderSet)));
				}
			}
			if (isUpdate) {
				SignTeamEntity teamEntity = signApplyManager.getTeamEntity(gameId, orgId, teamName);
				teamEntity.setLeaderName(teamDto.getLeaderName());
				teamEntity.setCoachName(teamDto.getCoachName());

				signApplyManager.updateTeam(teamEntity);
			}

		}

		// 如果是集体项目则设置队伍编号
		singleUploadDto.setTeamId(String.valueOf(teamId));

		// 更新team-item关联表
		if (!signApplyManager.checkItemTeamExist(this.gameId,itemId, teamId)) {
			// 不存在该关联关系，则新增
			signApplyManager.saveTeamItem(this.gameId,groupId,itemId,teamId);
		}
	}

}

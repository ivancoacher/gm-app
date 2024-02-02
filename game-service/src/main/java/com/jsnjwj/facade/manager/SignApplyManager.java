package com.jsnjwj.facade.manager;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.facade.dto.SignSingleDto;
import com.jsnjwj.facade.easyexcel.upload.ImportSingleUploadDto;
import com.jsnjwj.facade.easyexcel.upload.ImportTeamUploadDto;
import com.jsnjwj.facade.entity.*;
import com.jsnjwj.facade.mapper.*;
import com.jsnjwj.facade.query.SignSingleListQuery;
import com.jsnjwj.facade.query.SignTeamListQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignApplyManager {

	private final SignSingleMapper signSingleMapper;

	private final SignTeamMapper signTeamMapper;

	private final SignOrgMapper signOrgMapper;

	private final GameGroupMapper gameGroupMapper;

	private final GameItemMapper gameItemMapper;

	public void cleanSignData(Long gameId) {
		QueryWrapper<SignTeamEntity> teamQuery = new QueryWrapper<>();
		QueryWrapper<GameGroupEntity> groupQuery = new QueryWrapper<>();
		QueryWrapper<GameItemEntity> itemQuery = new QueryWrapper<>();
		QueryWrapper<SignSingleEntity> signQuery = new QueryWrapper<>();
		QueryWrapper<SignOrgEntity> orgQuery = new QueryWrapper<>();
		teamQuery.eq("game_id", gameId);
		groupQuery.eq("game_id", gameId);
		itemQuery.eq("game_id", gameId);
		signQuery.eq("game_id", gameId);
		orgQuery.eq("game_id", gameId);
		signTeamMapper.delete(teamQuery);
		gameGroupMapper.delete(groupQuery);
		gameItemMapper.delete(itemQuery);
		signSingleMapper.delete(signQuery);
		signOrgMapper.delete(orgQuery);

	}

	public List<SignSingleDto> fetchSignSinglePage(SignSingleListQuery query) {
		LambdaQueryWrapper<SignSingleEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(null != query.getGameId(), SignSingleEntity::getGameId, query.getGameId());
		wrapper.eq(null != query.getGroupId(), SignSingleEntity::getGroupId, query.getGroupId());
		wrapper.eq(null != query.getItemId(), SignSingleEntity::getItemId, query.getItemId());
		wrapper.eq(null != query.getTeamId(), SignSingleEntity::getTeamId, query.getTeamId());
		wrapper.like(StringUtils.isNotBlank(query.getKey()), SignSingleEntity::getName, query.getKey());
		return signSingleMapper.selectByPage((query.getPage() - 1) * query.getPageSize(), query.getPageSize(), wrapper);
	}

	public long fetchSignSingleCount(SignSingleListQuery query) {
		LambdaQueryWrapper<SignSingleEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(null != query.getGameId(), SignSingleEntity::getGameId, query.getGameId());
		wrapper.eq(null != query.getGroupId(), SignSingleEntity::getGroupId, query.getGroupId());
		wrapper.eq(null != query.getItemId(), SignSingleEntity::getItemId, query.getItemId());
		wrapper.eq(null != query.getTeamId(), SignSingleEntity::getTeamId, query.getTeamId());
		wrapper.like(StringUtils.isNotBlank(query.getKey()), SignSingleEntity::getName, query.getKey());
		return signSingleMapper.selectCount(wrapper);
	}

	public Page<SignTeamEntity> fetchSignTeamPage(SignTeamListQuery query) {
		LambdaQueryWrapper<SignTeamEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(null != query.getGameId(), SignTeamEntity::getGameId, query.getGameId());
		wrapper.like(null != query.getKey(), SignTeamEntity::getTeamName, query.getKey());

		Page<SignTeamEntity> page = new Page<>(query.getPage(), query.getLimit());
		page.setCurrent(query.getPage());
		page.setSize(query.getLimit());
		return signTeamMapper.selectPage(page, wrapper);

	}

	public SignOrgEntity getSignOrgById(Long orgId) {
		LambdaQueryWrapper<SignOrgEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(SignOrgEntity::getId, orgId);
		queryWrapper.last("limit 1");
		return signOrgMapper.selectOne(queryWrapper);
	}

	public List<SignTeamEntity> fetchSignTeamData(SignTeamListQuery query) {
		LambdaQueryWrapper<SignTeamEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(null != query.getGameId(), SignTeamEntity::getGameId, query.getGameId());
		wrapper.like(null != query.getKey(), SignTeamEntity::getTeamName, query.getKey());

		return signTeamMapper.selectList(wrapper);

	}

	public void saveTeamBatch(Long gameId, List<ImportTeamUploadDto> data) {
		List<SignTeamEntity> datas = new ArrayList<>();
		if (data.size() > 0) {
			data.forEach(d -> {
				SignTeamEntity team = new SignTeamEntity();
				team.setGameId(gameId);
				team.setTeamName(d.getTeamName());
				team.setLeaderName(d.getLeaderName());
				team.setLeaderTel(d.getLeaderTel());
				datas.add(team);
			});
		}
		signTeamMapper.saveBatch(datas);
	}

	public void saveSingleBatch(Long gameId, List<ImportSingleUploadDto> data) {
		log.info(JSON.toJSONString(data));
		List<SignSingleEntity> datas = new ArrayList<>();
		if (!data.isEmpty()) {
			data.forEach(d -> {
				SignSingleEntity team = new SignSingleEntity();
				team.setGameId(gameId);
				team.setTeamId(d.getTeamId());
				team.setName(d.getName());
				team.setAge(0);
				team.setSex("男".equals(d.getSex()) ? 1 : 0);
				team.setGroupId(d.getGroupId());
				team.setItemId(d.getItemId());
				team.setCountry(d.getCountry());
				team.setNation(d.getNation());
				team.setStudentNum(d.getStudentNum());
				team.setCardNum(d.getCardNum());
				team.setOrgName(d.getOrgName());
				datas.add(team);
			});
		}
		log.info("insert sign data : {}", JSON.toJSONString(datas));
		signSingleMapper.saveBatch(datas);
	}

	public boolean checkGroupExist(Long gameId, String groupName) {
		LambdaQueryWrapper<GameGroupEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(GameGroupEntity::getGameId, gameId);
		queryWrapper.eq(GameGroupEntity::getGroupName, groupName);
		return gameGroupMapper.exists(queryWrapper);
	}

	public boolean checkOrgExist(Long gameId, String orgName) {
		LambdaQueryWrapper<SignOrgEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(SignOrgEntity::getGameId, gameId);
		queryWrapper.eq(SignOrgEntity::getOrgName, orgName);
		return signOrgMapper.exists(queryWrapper);
	}

	public Long saveOrg(Long gameId, String orgName) {
		SignOrgEntity orgEntity = new SignOrgEntity();
		orgEntity.setOrgName(orgName);
		orgEntity.setGameId(gameId);
		signOrgMapper.insert(orgEntity);
		return orgEntity.getId();
	}

	public SignOrgEntity getOrgEntity(Long gameId, String orgName) {
		LambdaQueryWrapper<SignOrgEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(SignOrgEntity::getGameId, gameId);
		queryWrapper.eq(SignOrgEntity::getOrgName, orgName);
		queryWrapper.last("limit 1");
		return signOrgMapper.selectOne(queryWrapper);
	}

	public GameGroupEntity getGroupEntity(Long gameId, String groupName) {
		LambdaQueryWrapper<GameGroupEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(GameGroupEntity::getGameId, gameId);
		queryWrapper.eq(GameGroupEntity::getGroupName, groupName);
		queryWrapper.last("limit 1");
		return gameGroupMapper.selectOne(queryWrapper);
	}

	public Long saveGroup(Long gameId, String groupName) {
		GameGroupEntity groupEntity = new GameGroupEntity();
		groupEntity.setGroupName(groupName);
		groupEntity.setGameId(gameId);
		gameGroupMapper.insert(groupEntity);
		return groupEntity.getId();
	}

	public boolean checkItemExist(Long gameId, Long groupId, String itemName) {
		LambdaQueryWrapper<GameItemEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(GameItemEntity::getGameId, gameId);
		queryWrapper.eq(GameItemEntity::getItemName, itemName);
		queryWrapper.eq(GameItemEntity::getGroupId, groupId);
		return gameItemMapper.exists(queryWrapper);
	}

	public Long saveItem(Long gameId, Long groupId, String itemName, String itemType) {
		GameItemEntity itemEntity = new GameItemEntity();
		itemEntity.setItemName(itemName);
		itemEntity.setGroupId(groupId);
		itemEntity.setGameId(gameId);
		itemEntity.setItemType(itemType);
		gameItemMapper.insert(itemEntity);
		return itemEntity.getId();
	}

	public GameItemEntity getItemEntity(Long gameId, Long groupId, String itemName) {
		LambdaQueryWrapper<GameItemEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(GameItemEntity::getGameId, gameId);
		queryWrapper.eq(GameItemEntity::getGroupId, groupId);
		queryWrapper.eq(GameItemEntity::getItemName, itemName);
		queryWrapper.last("limit 1");
		return gameItemMapper.selectOne(queryWrapper);
	}

	public boolean checkTeamExist(Long gameId, String teamName) {
		LambdaQueryWrapper<SignTeamEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(SignTeamEntity::getGameId, gameId);
		queryWrapper.eq(SignTeamEntity::getTeamName, teamName);
		return signTeamMapper.exists(queryWrapper);
	}

	public Long saveTeamByImport(Long gameId, ImportSingleUploadDto singleUploadDto) {
		SignTeamEntity teamEntity = new SignTeamEntity();
		teamEntity.setGameId(gameId);
		teamEntity.setTeamName(singleUploadDto.getTeamName());
		teamEntity.setCoachName(singleUploadDto.getCoachName());
		teamEntity.setCoachTel(singleUploadDto.getCoachPhone());
		teamEntity.setLeaderName(singleUploadDto.getLeaderName());
		teamEntity.setLeaderTel(singleUploadDto.getLeaderPhone());
		teamEntity.setOrgId(singleUploadDto.getOrgId());

		signTeamMapper.insert(teamEntity);
		return teamEntity.getId();
	}

	public SignTeamEntity getTeamEntity(Long gameId, String teamName) {
		LambdaQueryWrapper<SignTeamEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(SignTeamEntity::getGameId, gameId);
		queryWrapper.eq(SignTeamEntity::getTeamName, teamName);
		queryWrapper.last("limit 1");
		return signTeamMapper.selectOne(queryWrapper);
	}

	public SignTeamEntity getTeamById(Long teamId) {
		return signTeamMapper.selectById(teamId);

	}

	public GameItemEntity getItemById(Long itemId) {
		return gameItemMapper.selectById(itemId);

	}

	public GameGroupEntity getGroupById(Long groupId) {
		return gameGroupMapper.selectById(groupId);

	}

	/**
	 * 获取报名数据中所有group
	 */
	public List<SignSingleEntity> getSignGroups(Long gameId) {
		LambdaQueryWrapper<SignSingleEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(SignSingleEntity::getGameId, gameId);
		wrapper.groupBy(SignSingleEntity::getGroupId);
		return signSingleMapper.selectList(wrapper);
	}

	public List<SignSingleEntity> getApplyByGroup(Long gameId, Long groupId) {
		LambdaQueryWrapper<SignSingleEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(SignSingleEntity::getGameId, gameId);
		wrapper.eq(SignSingleEntity::getGroupId, groupId);
		return signSingleMapper.selectList(wrapper);
	}

	/**
	 * 获取报名数据中所有item
	 */
	public List<SignSingleEntity> getSignItems(Long gameId) {
		LambdaQueryWrapper<SignSingleEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(SignSingleEntity::getGameId, gameId);
		wrapper.groupBy(SignSingleEntity::getGroupId, SignSingleEntity::getItemId);
		return signSingleMapper.selectList(wrapper);
	}

	public List<SignSingleEntity> getApplyByItem(Long gameId, Long itemId) {
		LambdaQueryWrapper<SignSingleEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(SignSingleEntity::getGameId, gameId);
		wrapper.eq(SignSingleEntity::getItemId, itemId);
		return signSingleMapper.selectList(wrapper);
	}

	/**
	 * 获取报名数据中所有orgName
	 */
	public List<SignSingleEntity> getSignOrgs(Long gameId) {
		LambdaQueryWrapper<SignSingleEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(SignSingleEntity::getGameId, gameId);
		wrapper.groupBy(SignSingleEntity::getOrgName);
		return signSingleMapper.selectList(wrapper);
	}

	public List<SignSingleEntity> getApplyByOrg(Long gameId, String orgName) {
		LambdaQueryWrapper<SignSingleEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(SignSingleEntity::getGameId, gameId);
		wrapper.eq(SignSingleEntity::getOrgName, orgName);
		return signSingleMapper.selectList(wrapper);
	}

	public List<SignSingleEntity> getApplyByOrgAndGroupId(Long gameId, Long groupId, String orgName) {
		LambdaQueryWrapper<SignSingleEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(SignSingleEntity::getGameId, gameId);
		wrapper.eq(SignSingleEntity::getGroupId, groupId);
		wrapper.eq(SignSingleEntity::getOrgName, orgName);
		return signSingleMapper.selectList(wrapper);
	}

	public List<SignSingleEntity> getOrgsByGroupId(Long gameId, Long groupId) {
		LambdaQueryWrapper<SignSingleEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(SignSingleEntity::getGameId, gameId);
		wrapper.eq(SignSingleEntity::getGroupId, groupId);
		wrapper.groupBy(SignSingleEntity::getOrgName);
		return signSingleMapper.selectList(wrapper);
	}

	public List<SignSingleEntity> getOrgsByGroupIdAndItemId(Long gameId, Long groupId, Long itemId) {
		LambdaQueryWrapper<SignSingleEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(SignSingleEntity::getGameId, gameId);
		wrapper.eq(SignSingleEntity::getGroupId, groupId);
		wrapper.eq(SignSingleEntity::getItemId, itemId);
		wrapper.groupBy(SignSingleEntity::getOrgName);
		return signSingleMapper.selectList(wrapper);
	}

	public List<SignTeamEntity> getTeamsByIds(Long gameId, List<Long> teamIds) {
		LambdaQueryWrapper<SignTeamEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(SignTeamEntity::getGameId, gameId);
		wrapper.in(SignTeamEntity::getId, teamIds);
		return signTeamMapper.selectList(wrapper);
	}

}

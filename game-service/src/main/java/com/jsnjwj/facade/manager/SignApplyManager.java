package com.jsnjwj.facade.manager;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
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

	private final SignItemTeamMapper signItemTeamMapper;

	private final GameItemMapper gameItemMapper;

	public void cleanSignData(Long gameId) {
		QueryWrapper<SignTeamEntity> teamQuery = new QueryWrapper<>();
		QueryWrapper<GameGroupEntity> groupQuery = new QueryWrapper<>();
		QueryWrapper<GameItemEntity> itemQuery = new QueryWrapper<>();
		QueryWrapper<SignSingleEntity> signQuery = new QueryWrapper<>();
		QueryWrapper<SignOrgEntity> orgQuery = new QueryWrapper<>();
		QueryWrapper<SignItemTeamEntity> itemTeamQuery = new QueryWrapper<>();
		teamQuery.eq("game_id", gameId);
		groupQuery.eq("game_id", gameId);
		itemQuery.eq("game_id", gameId);
		signQuery.eq("game_id", gameId);
		orgQuery.eq("game_id", gameId);
		itemTeamQuery.eq("game_id", gameId);
		signTeamMapper.delete(teamQuery);
		gameGroupMapper.delete(groupQuery);
		gameItemMapper.delete(itemQuery);
		signSingleMapper.delete(signQuery);
		signOrgMapper.delete(orgQuery);
		signItemTeamMapper.delete(itemTeamQuery);
	}

	public List<SignSingleDto> fetchSignSinglePage(SignSingleListQuery query) {
		LambdaQueryWrapper<SignSingleEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(null != query.getGameId(), SignSingleEntity::getGameId, query.getGameId());
		wrapper.eq(null != query.getGroupId(), SignSingleEntity::getGroupId, query.getGroupId());
		wrapper.eq(null != query.getItemId(), SignSingleEntity::getItemId, query.getItemId());
		wrapper.eq(null != query.getTeamId(), SignSingleEntity::getTeamId, query.getTeamId());
		wrapper.eq(null != query.getOrgId(), SignSingleEntity::getOrgId, query.getOrgId());
		wrapper.like(StringUtils.isNotBlank(query.getKey()), SignSingleEntity::getName, query.getKey());
		return signSingleMapper.selectByPage((query.getPage() - 1) * query.getPageSize(), query.getPageSize(), wrapper);
	}

	public long fetchSignSingleCount(SignSingleListQuery query) {
		LambdaQueryWrapper<SignSingleEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(null != query.getGameId(), SignSingleEntity::getGameId, query.getGameId());
		wrapper.eq(null != query.getGroupId(), SignSingleEntity::getGroupId, query.getGroupId());
		wrapper.eq(null != query.getItemId(), SignSingleEntity::getItemId, query.getItemId());
		wrapper.eq(null != query.getTeamId(), SignSingleEntity::getTeamId, query.getTeamId());
		wrapper.eq(null != query.getOrgId(), SignSingleEntity::getOrgId, query.getOrgId());
		wrapper.like(StringUtils.isNotBlank(query.getKey()), SignSingleEntity::getName, query.getKey());
		return signSingleMapper.selectCount(wrapper);
	}

	public Page<SignTeamEntity> fetchSignTeamPage(SignTeamListQuery query) {
		LambdaQueryWrapper<SignTeamEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(null != query.getGameId(), SignTeamEntity::getGameId, query.getGameId());
		wrapper.like(null != query.getTeamId(), SignTeamEntity::getId, query.getTeamId());
		wrapper.like(null != query.getOrgId(), SignTeamEntity::getOrgId, query.getOrgId());

		if (StringUtils.isNotBlank(query.getTitle())) {
			wrapper.and(t -> {
				t.like(SignTeamEntity::getCoachName, query.getTitle())
					.or()
					.like(SignTeamEntity::getCoachTel, query.getTitle())
					.or()
					.like(SignTeamEntity::getLeaderName, query.getTitle())
					.or()
					.like(SignTeamEntity::getLeaderTel, query.getTitle())
					.or()
					.like(SignTeamEntity::getTeamName, query.getTitle());

			});

		}

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
		wrapper.like(null != query.getTitle(), SignTeamEntity::getTeamName, query.getTitle());

		return signTeamMapper.selectList(wrapper);

	}

	public void saveTeamBatch(Long gameId, List<ImportTeamUploadDto> data) {
		List<SignTeamEntity> datas = new ArrayList<>();
		if (!data.isEmpty()) {
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
				SignSingleEntity signSingle = new SignSingleEntity();
				signSingle.setGameId(gameId);
				signSingle.setTeamId(d.getTeamId());
				signSingle.setName(d.getName());
				signSingle.setAge(0);
				signSingle.setSex("男".equals(d.getSex()) ? 1 : 0);
				signSingle.setGroupId(d.getGroupId());
				signSingle.setItemId(d.getItemId());
				signSingle.setCountry(d.getCountry());
				signSingle.setNation(d.getNation());
				signSingle.setStudentNum(d.getStudentNum());
				signSingle.setCardNum(d.getCardNum());
				signSingle.setOrgName(d.getOrgName());
				signSingle.setOrgId(d.getOrgId());

				datas.add(signSingle);
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

	public boolean checkTeamExist(Long gameId, Long orgId, String teamName) {
		LambdaQueryWrapper<SignTeamEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(SignTeamEntity::getGameId, gameId);
		queryWrapper.eq(SignTeamEntity::getOrgId, orgId);
		queryWrapper.eq(SignTeamEntity::getTeamName, teamName);
		return signTeamMapper.exists(queryWrapper);
	}

	public Long saveTeamByImport(Long gameId, ImportSingleUploadDto singleUploadDto) {
		SignTeamEntity teamEntity = new SignTeamEntity();
		teamEntity.setGameId(gameId);
		teamEntity.setOrgId(singleUploadDto.getOrgId());
		teamEntity.setTeamName(singleUploadDto.getTeamName());
		teamEntity.setCoachName(singleUploadDto.getCoachName());
		teamEntity.setCoachTel(singleUploadDto.getCoachPhone());
		teamEntity.setLeaderName(singleUploadDto.getLeaderName());
		teamEntity.setLeaderTel(singleUploadDto.getLeaderPhone());
		teamEntity.setRemark(singleUploadDto.getRemark());
		signTeamMapper.insert(teamEntity);
		return teamEntity.getId();
	}

	public SignTeamEntity getTeamEntity(Long gameId, Long orgId, String teamName) {
		LambdaQueryWrapper<SignTeamEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(SignTeamEntity::getGameId, gameId);
		queryWrapper.eq(SignTeamEntity::getOrgId, orgId);
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

	public List<GameGroupEntity> getGroupList(Long gameId) {
		LambdaQueryWrapper<GameGroupEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(GameGroupEntity::getGameId, gameId);
		queryWrapper.orderByAsc(GameGroupEntity::getSort);
		return gameGroupMapper.selectList(queryWrapper);

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

	public List<SignSingleEntity> getApplyByItemIds(Long gameId, List<Long> itemIds) {
		LambdaQueryWrapper<SignSingleEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(SignSingleEntity::getGameId, gameId);
		wrapper.in(SignSingleEntity::getItemId, itemIds);
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

	public List<SignOrgEntity> getOrgList(Long gameId) {
		LambdaQueryWrapper<SignOrgEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(SignOrgEntity::getGameId, gameId);
		return signOrgMapper.selectList(wrapper);
	}

	/**
	 * 获取报名数据中所有orgName
	 */
	public List<SignSingleEntity> getSignTeams(Long gameId) {
		LambdaQueryWrapper<SignSingleEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(SignSingleEntity::getGameId, gameId);
		wrapper.groupBy(SignSingleEntity::getTeamId);
		return signSingleMapper.selectList(wrapper);
	}

	public List<SignSingleEntity> getApplyByOrg(Long gameId, String orgName) {
		LambdaQueryWrapper<SignSingleEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(SignSingleEntity::getGameId, gameId);
		wrapper.eq(SignSingleEntity::getOrgName, orgName);
		return signSingleMapper.selectList(wrapper);
	}

	public List<SignSingleEntity> getApplyByOrgId(Long gameId, Long orgId) {
		LambdaQueryWrapper<SignSingleEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(SignSingleEntity::getGameId, gameId);
		wrapper.eq(SignSingleEntity::getOrgId, orgId);
		return signSingleMapper.selectList(wrapper);
	}

	public List<SignSingleEntity> getApplyByTeam(Long gameId, Long teamId) {
		LambdaQueryWrapper<SignSingleEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(SignSingleEntity::getGameId, gameId);
		wrapper.eq(SignSingleEntity::getTeamId, teamId);
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

	public List<SignSingleEntity> getTeamByGroupId(Long gameId, Long groupId) {
		LambdaQueryWrapper<SignSingleEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(SignSingleEntity::getGameId, gameId);
		wrapper.eq(SignSingleEntity::getGroupId, groupId);
		wrapper.groupBy(SignSingleEntity::getTeamId);
		return signSingleMapper.selectList(wrapper);
	}

	public List<SignSingleEntity> getTeamByGroupIdAndItemId(Long gameId, Long groupId, Long itemId) {
		LambdaQueryWrapper<SignSingleEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(SignSingleEntity::getGameId, gameId);
		wrapper.eq(SignSingleEntity::getGroupId, groupId);
		wrapper.eq(SignSingleEntity::getItemId, itemId);
		wrapper.groupBy(SignSingleEntity::getTeamId);
		return signSingleMapper.selectList(wrapper);
	}

	public List<SignSingleEntity> getApplyByItemIdAndTeamId(Long gameId, Long groupId, Long itemId, Long teamId) {
		LambdaQueryWrapper<SignSingleEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(SignSingleEntity::getGameId, gameId);
		wrapper.eq(SignSingleEntity::getGroupId, groupId);
		wrapper.eq(SignSingleEntity::getItemId, itemId);
		wrapper.eq(SignSingleEntity::getTeamId, teamId);
		return signSingleMapper.selectList(wrapper);
	}

	public List<SignSingleEntity> getApplyByGroupIdAndTeamId(Long gameId, Long groupId, Long teamId) {
		LambdaQueryWrapper<SignSingleEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(SignSingleEntity::getGameId, gameId);
		wrapper.eq(SignSingleEntity::getGroupId, groupId);
		wrapper.eq(SignSingleEntity::getTeamId, teamId);
		return signSingleMapper.selectList(wrapper);
	}

	public List<SignTeamEntity> getTeamsByIds(Long gameId, List<Long> teamIds) {
		LambdaQueryWrapper<SignTeamEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(SignTeamEntity::getGameId, gameId);
		wrapper.in(SignTeamEntity::getId, teamIds);
		return signTeamMapper.selectList(wrapper);
	}

	public int updateTeam(SignTeamEntity teamEntity) {
		return signTeamMapper.updateById(teamEntity);
	}

	public int updateOrg(SignOrgEntity orgEntity) {
		return signOrgMapper.updateById(orgEntity);
	}

	public int deleteTeam(Long teamId) {
		return signTeamMapper.deleteById(teamId);
	}

	public List<SignSingleEntity> getSignByGroupId(Long gameId, Long groupId) {
		LambdaQueryWrapper<SignSingleEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(SignSingleEntity::getGameId, gameId);
		queryWrapper.eq(SignSingleEntity::getGroupId, groupId);
		return signSingleMapper.selectList(queryWrapper);
	}

	public List<SignSingleEntity> getSingByItemId(Long gameId, Long itemId) {
		LambdaQueryWrapper<SignSingleEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(SignSingleEntity::getGameId, gameId);
		queryWrapper.eq(SignSingleEntity::getItemId, itemId);
		return signSingleMapper.selectList(queryWrapper);
	}

	public int deleteById(Long signId) {
		return signSingleMapper.deleteById(signId);
	}

	public int deleteByIds(List<Long> signIds) {
		LambdaQueryWrapper<SignSingleEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.in(SignSingleEntity::getId, signIds);
		return signSingleMapper.delete(queryWrapper);
	}

	public int updateSingle(SignSingleEntity signSingleEntity) {
		SignSingleEntity signSingle = signSingleMapper.selectById(signSingleEntity.getId());

		LambdaUpdateWrapper<SignSingleEntity> queryWrapper = new LambdaUpdateWrapper<>();
		queryWrapper.set(SignSingleEntity::getName, signSingleEntity.getName());
		queryWrapper.set(SignSingleEntity::getAge, signSingleEntity.getAge());
		queryWrapper.set(SignSingleEntity::getSex, signSingleEntity.getSex());
		queryWrapper.eq(SignSingleEntity::getName, signSingle.getName());
		queryWrapper.eq(SignSingleEntity::getOrgName, signSingleEntity.getOrgName());
		return signSingleMapper.update(null, queryWrapper);
	}

	public long countOrgByItemId(Long itemId) {
		LambdaQueryWrapper<SignSingleEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(SignSingleEntity::getItemId, itemId);
		queryWrapper.groupBy(SignSingleEntity::getOrgId);
		return signSingleMapper.selectList(queryWrapper).size();
	}

	public long countTeamByItemId(Long itemId) {
		LambdaQueryWrapper<SignSingleEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(SignSingleEntity::getItemId, itemId);
		queryWrapper.groupBy(SignSingleEntity::getTeamId);

		return signSingleMapper.selectList(queryWrapper).size();
	}

	public long countPlayerTimesByItemId(Long itemId) {
		LambdaQueryWrapper<SignSingleEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(SignSingleEntity::getItemId, itemId);
		return signSingleMapper.selectList(queryWrapper).size();
	}

	public List<SignSingleEntity> countPlayerCountingByOrgId(Long orgId) {
		LambdaQueryWrapper<SignSingleEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(SignSingleEntity::getOrgId, orgId);
		queryWrapper.groupBy(SignSingleEntity::getName);
		return signSingleMapper.selectList(queryWrapper);

	}

	public List<SignSingleEntity> countPlayerTimesByOrgId(Long orgId) {
		LambdaQueryWrapper<SignSingleEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(SignSingleEntity::getOrgId, orgId);
		return signSingleMapper.selectList(queryWrapper);

	}

	public List<SignSingleEntity> getSingleByGameId(Long gameId) {
		LambdaQueryWrapper<SignSingleEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(SignSingleEntity::getGameId, gameId);
		return signSingleMapper.selectList(queryWrapper);

	}

	public List<SignSingleEntity> getSingleByItemIds(Long gameId, List<Long> itemIds) {
		LambdaQueryWrapper<SignSingleEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(SignSingleEntity::getGameId, gameId);
		queryWrapper.in(SignSingleEntity::getItemId, itemIds);
		return signSingleMapper.selectList(queryWrapper);

	}

	/**
	 * 校验项目-队伍对应关系
	 * @param gameId
	 * @param itemId
	 * @param teamId
	 * @return
	 */
	public boolean checkItemTeamExist(Long gameId, Long itemId, Long teamId) {
		LambdaQueryWrapper<SignItemTeamEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(SignItemTeamEntity::getGameId, gameId);
		queryWrapper.eq(SignItemTeamEntity::getItemId, itemId);
		queryWrapper.eq(SignItemTeamEntity::getTeamId, teamId);
		return signItemTeamMapper.exists(queryWrapper);
	}

	public void saveTeamItem(Long gameId, Long groupId, Long itemId, Long teamId){
		SignItemTeamEntity itemTeamEntity = new SignItemTeamEntity();
		itemTeamEntity.setGameId(gameId);
		itemTeamEntity.setGroupId(groupId);
		itemTeamEntity.setItemId(itemId);
		itemTeamEntity.setTeamId(teamId);
		signItemTeamMapper.insert(itemTeamEntity);
	}
}

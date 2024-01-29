package com.jsnjwj.facade.manager;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.facade.easyexcel.upload.ImportSingleUploadDto;
import com.jsnjwj.facade.easyexcel.upload.ImportTeamUploadDto;
import com.jsnjwj.facade.dto.SignSingleDto;
import com.jsnjwj.facade.entity.GameGroupEntity;
import com.jsnjwj.facade.entity.GameItemEntity;
import com.jsnjwj.facade.entity.SignSingleEntity;
import com.jsnjwj.facade.entity.SignTeamEntity;
import com.jsnjwj.facade.mapper.GameGroupMapper;
import com.jsnjwj.facade.mapper.GameItemMapper;
import com.jsnjwj.facade.mapper.SignSingleMapper;
import com.jsnjwj.facade.mapper.SignTeamMapper;
import com.jsnjwj.facade.query.SignSingleListQuery;
import com.jsnjwj.facade.query.SignTeamListQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignApplyManager {

    private final SignSingleMapper signSingleMapper;

    private final SignTeamMapper signTeamMapper;

    private final GameGroupMapper gameGroupMapper;

    private final GameItemMapper gameItemMapper;


    public void cleanSignData(Long gameId) {
        QueryWrapper<SignTeamEntity> teamQuery = new QueryWrapper<>();
        QueryWrapper<GameGroupEntity> groupQuery = new QueryWrapper<>();
        QueryWrapper<GameItemEntity> itemQuery = new QueryWrapper<>();
        teamQuery.eq("game_id", gameId);
        groupQuery.eq("game_id", gameId);
        itemQuery.eq("game_id", gameId);
        signTeamMapper.delete(teamQuery);
        gameGroupMapper.delete(groupQuery);
        gameItemMapper.delete(itemQuery);
    }

    public List<SignSingleDto> fetchSignSinglePage(SignSingleListQuery query) {
        LambdaQueryWrapper<SignSingleEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(null != query.getGameId(), SignSingleEntity::getGameId, query.getGameId());
        wrapper.eq(null != query.getGroupId(), SignSingleEntity::getGroupId, query.getGroupId());
        wrapper.eq(null != query.getItemId(), SignSingleEntity::getItemId, query.getItemId());
        wrapper.eq(null != query.getTeamId(), SignSingleEntity::getItemId, query.getTeamId());
        wrapper.like(null != query.getKey(), SignSingleEntity::getName, query.getKey());
        return signSingleMapper.selectByPage((query.getPage() - 1) * query.getLimit(), query.getLimit(), wrapper);
    }

    public long fetchSignSingleCount(SignSingleListQuery query) {
        LambdaQueryWrapper<SignSingleEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(null != query.getGameId(), SignSingleEntity::getGameId, query.getGameId());
        wrapper.eq(null != query.getGroupId(), SignSingleEntity::getGroupId, query.getGroupId());
        wrapper.eq(null != query.getItemId(), SignSingleEntity::getItemId, query.getItemId());
        wrapper.eq(null != query.getTeamId(), SignSingleEntity::getItemId, query.getTeamId());
        wrapper.like(null != query.getKey(), SignSingleEntity::getName, query.getKey());
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
                team.setSex("男".equals(d.getSex())?1:0);
                team.setGroupId(d.getGroupId());
                team.setItemId(d.getItemId());
                team.setOrgName(d.getSchool());
                team.setCountry(d.getCountry());
                team.setNation(d.getNation());
                team.setStudentNum(d.getStudentNum());
                team.setCardNum(d.getCardNum());
                datas.add(team);
            });
        }
        log.info("insert sign data : {}",JSON.toJSONString(datas));
        signSingleMapper.saveBatch(datas);
    }


    public boolean checkGroupExist(Long gameId, String groupName) {
        LambdaQueryWrapper<GameGroupEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GameGroupEntity::getGameId, gameId);
        queryWrapper.eq(GameGroupEntity::getGroupName, groupName);
        return gameGroupMapper.exists(queryWrapper);
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

    public Long saveItem(Long gameId, Long groupId, String itemName) {
        GameItemEntity itemEntity = new GameItemEntity();
        itemEntity.setItemName(itemName);
        itemEntity.setGroupId(groupId);
        itemEntity.setGameId(gameId);
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

    public SignTeamEntity getTeamById(Long teamId){
        return signTeamMapper.selectById(teamId);

    }

    public GameItemEntity getItemById(Long itemId){
        return gameItemMapper.selectById(itemId);

    }

    public GameGroupEntity getGroupById(Long groupId){
        return gameGroupMapper.selectById(groupId);

    }
}

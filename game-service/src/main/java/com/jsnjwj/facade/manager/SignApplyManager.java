package com.jsnjwj.facade.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.facade.easyexcel.upload.ImportSingleUploadDto;
import com.jsnjwj.facade.easyexcel.upload.ImportTeamUploadDto;
import com.jsnjwj.facade.dto.SignSingleDto;
import com.jsnjwj.facade.entity.SignSingleEntity;
import com.jsnjwj.facade.entity.SignTeamEntity;
import com.jsnjwj.facade.mapper.SignSingleMapper;
import com.jsnjwj.facade.mapper.SignTeamMapper;
import com.jsnjwj.facade.query.SignSingleListQuery;
import com.jsnjwj.facade.query.SignTeamListQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class SignApplyManager {

    @Resource
    private SignSingleMapper signSingleMapper;

    @Resource
    private SignTeamMapper signTeamMapper;

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
        List<SignSingleEntity> datas = new ArrayList<>();
        if (data.size() > 0) {
            data.forEach(d -> {
                SignSingleEntity team = new SignSingleEntity();
                team.setGameId(gameId);
                team.setTeamName(d.getTeamName());
                team.setName(d.getName());
                team.setAge(Integer.valueOf(d.getAge()));
                team.setSex(Integer.valueOf(d.getSex()));
                datas.add(team);
            });
        }
        signSingleMapper.saveBatch(datas);
    }

}

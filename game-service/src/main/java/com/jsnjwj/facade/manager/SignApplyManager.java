package com.jsnjwj.facade.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.facade.dto.ImportTeamDto;
import com.jsnjwj.facade.dto.SignSingleDto;
import com.jsnjwj.facade.dto.SignTeamDto;
import com.jsnjwj.facade.entity.TcSignSingle;
import com.jsnjwj.facade.entity.TcSignTeam;
import com.jsnjwj.facade.mapper.TcSignSingleMapper;
import com.jsnjwj.facade.mapper.TcSignTeamMapper;
import com.jsnjwj.facade.query.SignSingleListQuery;
import com.jsnjwj.facade.query.SignTeamListQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class SignApplyManager {
    @Resource
    private TcSignSingleMapper signSingleMapper;

    @Resource
    private TcSignTeamMapper signTeamMapper;

    public List<SignSingleDto> fetchSignSinglePage(SignSingleListQuery query) {
        LambdaQueryWrapper<TcSignSingle> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(null != query.getGameId(), TcSignSingle::getGameId, query.getGameId());
        wrapper.eq(null != query.getGroupId(), TcSignSingle::getGroupId, query.getGroupId());
        wrapper.eq(null != query.getItemId(), TcSignSingle::getItemId, query.getItemId());
        wrapper.eq(null != query.getTeamId(), TcSignSingle::getItemId, query.getTeamId());
        wrapper.like(null != query.getKey(), TcSignSingle::getName, query.getKey());
        return signSingleMapper.selectByPage((query.getPage()-1)*query.getLimit(), query.getLimit(), wrapper);
    }

    public long fetchSignSingleCount(SignSingleListQuery query) {
        LambdaQueryWrapper<TcSignSingle> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(null != query.getGameId(), TcSignSingle::getGameId, query.getGameId());
        wrapper.eq(null != query.getGroupId(), TcSignSingle::getGroupId, query.getGroupId());
        wrapper.eq(null != query.getItemId(), TcSignSingle::getItemId, query.getItemId());
        wrapper.eq(null != query.getTeamId(), TcSignSingle::getItemId, query.getTeamId());
        wrapper.like(null != query.getKey(), TcSignSingle::getName, query.getKey());
        return signSingleMapper.selectCount(wrapper);
    }


    public Page<TcSignTeam> fetchSignTeamPage(SignTeamListQuery query) {
        LambdaQueryWrapper<TcSignTeam> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(null != query.getGameId(), TcSignTeam::getGameId, query.getGameId());
        wrapper.like(null != query.getKey(), TcSignTeam::getTeamName, query.getKey());

        Page<TcSignTeam> page = new Page<>(query.getPage(),query.getLimit());

        return signTeamMapper.selectPage(page,wrapper);

    }

    public void saveTeamBatch(Long gameId,List<ImportTeamDto> data){
        List<TcSignTeam> datas = new ArrayList<>();
        if (data.size()>0){
            data.forEach(d->{
                TcSignTeam team = new TcSignTeam();
                team.setGameId(gameId);
                team.setTeamName(d.getTeamName());
                team.setLeaderName(d.getLeaderName());
                team.setLeaderTel(d.getLeaderTel());
                datas.add(team);
            });
        }
        signTeamMapper.saveBatch(datas);
    }
}

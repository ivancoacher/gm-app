package com.jsnjwj.facade.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.facade.dto.SignSingleDto;
import com.jsnjwj.facade.entity.TcSignSingle;
import com.jsnjwj.facade.mapper.TcSignSingleMapper;
import com.jsnjwj.facade.query.SignSingleListQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SignApplyManager {
    @Resource
    private TcSignSingleMapper signSingleMapper;

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
}

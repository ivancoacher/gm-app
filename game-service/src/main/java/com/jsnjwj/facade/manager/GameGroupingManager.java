package com.jsnjwj.facade.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsnjwj.facade.entity.GameAreaEntity;
import com.jsnjwj.facade.entity.GameAreaItemEntity;
import com.jsnjwj.facade.mapper.GameAreaItemMapper;
import com.jsnjwj.facade.mapper.GameAreaMapper;
import com.jsnjwj.facade.query.GameGroupingSetQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GameGroupingManager {

    @Resource
    private GameAreaMapper gameAreaMapper;

    @Resource
    private GameAreaItemMapper gameAreaItemMapper;

    public void saveCourts(List<GameAreaEntity> areaList) {
        gameAreaMapper.saveBatch(areaList);
    }

    public int deleteCourt(GameAreaEntity area) {
        return gameAreaMapper.deleteById(area);
    }

    public int resetCourt(Long gameId) {
        LambdaQueryWrapper<GameAreaEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GameAreaEntity::getGameId, gameId);
        return gameAreaMapper.delete(wrapper);
    }

    public int saveCourt(GameAreaEntity area) {
        return gameAreaMapper.updateById(area);
    }

    public List<GameAreaEntity> getCourts(Long gameId) {
        LambdaQueryWrapper<GameAreaEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GameAreaEntity::getGameId, gameId);
        // wrapper.eq(TcGameArea::getStatus,1); //启用中
        return gameAreaMapper.selectList(wrapper);
    }

    public List<GameAreaEntity> getAvailableCourts(Long gameId) {
        LambdaQueryWrapper<GameAreaEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GameAreaEntity::getGameId, gameId);
        wrapper.eq(GameAreaEntity::getStatus, 1); // 启用中
        return gameAreaMapper.selectList(wrapper);
    }

    public void resetGrouping(GameGroupingSetQuery query) {
        LambdaQueryWrapper<GameAreaItemEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GameAreaItemEntity::getGameId, query.getGameId());
        wrapper.eq(GameAreaItemEntity::getAreaId, query.getAreaId());
        wrapper.eq(GameAreaItemEntity::getAreaNo, query.getAreaNo());
        gameAreaItemMapper.delete(wrapper);
    }

    public void saveGroupings(List<GameAreaItemEntity> areaList) {
        gameAreaItemMapper.saveBatch(areaList);
    }

    public List<GameAreaItemEntity> fetchAreaItems(Long gameId, Long areaId) {
        LambdaQueryWrapper<GameAreaItemEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GameAreaItemEntity::getGameId, gameId);
        wrapper.eq(GameAreaItemEntity::getAreaId, areaId);
        wrapper.orderByAsc(GameAreaItemEntity::getSort);

        return gameAreaItemMapper.selectList(wrapper);

    }

}

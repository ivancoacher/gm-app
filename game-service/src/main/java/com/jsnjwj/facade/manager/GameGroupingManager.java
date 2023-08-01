package com.jsnjwj.facade.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsnjwj.facade.entity.TcGameArea;
import com.jsnjwj.facade.entity.TcGameAreaItem;
import com.jsnjwj.facade.mapper.TcGameAreaItemMapper;
import com.jsnjwj.facade.mapper.TcGameAreaMapper;
import com.jsnjwj.facade.query.GameGroupingSetQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GameGroupingManager {

	@Resource
	private TcGameAreaMapper tcGameAreaMapper;

	@Resource
	private TcGameAreaItemMapper tcGameAreaItemMapper;

	public void saveCourts(List<TcGameArea> areaList) {
		tcGameAreaMapper.saveBatch(areaList);
	}

	public int deleteCourt(TcGameArea area) {
		return tcGameAreaMapper.deleteById(area);
	}

	public int resetCourt(Long gameId) {
		LambdaQueryWrapper<TcGameArea> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(TcGameArea::getGameId, gameId);
		return tcGameAreaMapper.delete(wrapper);
	}

	public int saveCourt(TcGameArea area) {
		return tcGameAreaMapper.updateById(area);
	}

	public List<TcGameArea> getCourts(Long gameId) {
		LambdaQueryWrapper<TcGameArea> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(TcGameArea::getGameId, gameId);
//		wrapper.eq(TcGameArea::getStatus,1);	//启用中
		return tcGameAreaMapper.selectList(wrapper);
	}
	public List<TcGameArea> getAvailableCourts(Long gameId) {
		LambdaQueryWrapper<TcGameArea> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(TcGameArea::getGameId, gameId);
		wrapper.eq(TcGameArea::getStatus,1);	//启用中
		return tcGameAreaMapper.selectList(wrapper);
	}
	public void resetGrouping(GameGroupingSetQuery query) {
		LambdaQueryWrapper<TcGameAreaItem> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(TcGameAreaItem::getGameId, query.getGameId());
		wrapper.eq(TcGameAreaItem::getAreaId, query.getAreaId());
		wrapper.eq(TcGameAreaItem::getAreaNo, query.getAreaNo());
		tcGameAreaItemMapper.delete(wrapper);
	}

	public void saveGroupings(List<TcGameAreaItem> areaList) {
		tcGameAreaItemMapper.saveBatch(areaList);
	}

	public List<TcGameAreaItem> fetchAreaItems(Long gameId,Long areaId){
		LambdaQueryWrapper<TcGameAreaItem> wrapper  = new LambdaQueryWrapper<>();
		wrapper.eq(TcGameAreaItem::getGameId,gameId);
		wrapper.eq(TcGameAreaItem::getAreaId,areaId);
		wrapper.orderByAsc(TcGameAreaItem::getSort);

		return tcGameAreaItemMapper.selectList(wrapper);

	}


}

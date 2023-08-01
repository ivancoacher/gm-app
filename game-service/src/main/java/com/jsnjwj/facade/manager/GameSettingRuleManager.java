package com.jsnjwj.facade.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsnjwj.facade.entity.TcGameArea;
import com.jsnjwj.facade.entity.TcGameAreaItem;
import com.jsnjwj.facade.entity.TcGameRuleSetting;
import com.jsnjwj.facade.entity.TcGameRuleSettingDetail;
import com.jsnjwj.facade.mapper.TcGameAreaItemMapper;
import com.jsnjwj.facade.mapper.TcGameAreaMapper;
import com.jsnjwj.facade.mapper.TcGameRuleSettingDetailMapper;
import com.jsnjwj.facade.mapper.TcGameRuleSettingMapper;
import com.jsnjwj.facade.query.GameGroupingSetQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class GameSettingRuleManager {

	@Resource
	private TcGameRuleSettingMapper gameRuleSettingMapper;

	@Resource
	private TcGameRuleSettingDetailMapper gameRuleSettingDetailMapper;

	public int saveRuleInfo(TcGameRuleSetting ruleSetting) {
		return gameRuleSettingMapper.insert(ruleSetting);
	}

	public int updateRuleInfo(TcGameRuleSetting ruleSetting) {
		return gameRuleSettingMapper.updateById(ruleSetting);
	}

	public int saveRuleDetail(Long gameId, Long itemId, List<TcGameRuleSettingDetail> list) {
		LambdaQueryWrapper<TcGameRuleSettingDetail> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(TcGameRuleSettingDetail::getSettingId, itemId);
		gameRuleSettingDetailMapper.delete(wrapper);
		return gameRuleSettingDetailMapper.insertBatchSomeColumn(list);
	}

	public TcGameRuleSetting fetchRule(Long gameId, Long itemId) {
		LambdaQueryWrapper<TcGameRuleSetting> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(TcGameRuleSetting::getGameId, gameId);
		wrapper.eq(TcGameRuleSetting::getItemId, itemId);
		return gameRuleSettingMapper.selectById(wrapper);
	}

	public List<TcGameRuleSettingDetail> fetchRuleDetail(Long settingId) {
		return new ArrayList<>();
	}

}

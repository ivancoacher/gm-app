package com.jsnjwj.facade.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsnjwj.facade.entity.TcGameRuleSetting;
import com.jsnjwj.facade.entity.TcGameRuleSettingDetail;
import com.jsnjwj.facade.mapper.TcGameRuleSettingDetailMapper;
import com.jsnjwj.facade.mapper.TcGameRuleSettingMapper;
import com.jsnjwj.facade.vo.GameRuleSettingVo;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

public class GameRuleSettingManager {

	@Resource
	private TcGameRuleSettingMapper gameRuleSettingMapper;

	@Resource
	private TcGameRuleSettingDetailMapper gameRuleSettingDetailMapper;

	public GameRuleSettingVo fetchOne(Long gameId, Long itemId) {
		LambdaQueryWrapper<TcGameRuleSetting> query = new LambdaQueryWrapper<>();
		query.eq(TcGameRuleSetting::getGameId, gameId);
		query.eq(TcGameRuleSetting::getItemId, itemId);
		TcGameRuleSetting gameRuleSetting = gameRuleSettingMapper.selectOne(query);

		GameRuleSettingVo gameRuleSettingVo = new GameRuleSettingVo();

		gameRuleSettingVo.setGameId(gameId);
		gameRuleSettingVo.setItemId(itemId);
		gameRuleSettingVo.setJudgeGroupNum(gameRuleSetting.getJudgeGroupNum());

		LambdaQueryWrapper<TcGameRuleSettingDetail> detailQuery = new LambdaQueryWrapper<>();
		detailQuery.eq(TcGameRuleSettingDetail::getSettingId, gameRuleSetting.getId());
		List<TcGameRuleSettingDetail> details = gameRuleSettingDetailMapper.selectList(detailQuery);
		List<GameRuleSettingVo.GameRuleDetailVo> newDetails = new ArrayList<>();
		details.forEach(item -> {
			GameRuleSettingVo.GameRuleDetailVo detail = new GameRuleSettingVo.GameRuleDetailVo();
			detail.setSettingId(item.getSettingId());
			detail.setNum(item.getNum());
			detail.setExtraType(item.getExtraType());
			detail.setExtraName(item.getExtraName());
			detail.setScoreRatio(item.getScoreRatio());
			detail.setScoreWeight(item.getScoreWeight());
			newDetails.add(detail);
		});

		gameRuleSettingVo.setDetailVoList(newDetails);
		return gameRuleSettingVo;
	}

	public int update() {
		return 1;
	}

}

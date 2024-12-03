package com.jsnjwj.facade.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsnjwj.facade.entity.GameRuleSetting;
import com.jsnjwj.facade.entity.GameRuleSettingDetail;
import com.jsnjwj.facade.mapper.GameRuleSettingDetailMapper;
import com.jsnjwj.facade.mapper.GameRuleSettingMapper;
import com.jsnjwj.facade.vo.GameItemRuleVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameItemRuleManager {

	private final GameRuleSettingMapper gameRuleSettingMapper;


	public GameItemRuleVo getGameItemRule(Long gameId){

	}
}

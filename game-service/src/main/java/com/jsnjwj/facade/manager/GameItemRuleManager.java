package com.jsnjwj.facade.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsnjwj.facade.entity.GameItemRule;
import com.jsnjwj.facade.mapper.GameItemRuleMapper;
import com.jsnjwj.facade.mapper.GameRuleSettingMapper;
import com.jsnjwj.facade.vo.rule.GameItemRuleVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 项目规则设定
 */
@Service
@RequiredArgsConstructor
public class GameItemRuleManager {

	private final GameRuleSettingMapper gameRuleSettingMapper;

	private final GameItemRuleMapper gameItemRuleMapper;

	public List<GameItemRuleVo> getGameItemRuleList(Long gameId) {

		return null;
	}

	public GameItemRule getGameItemRule(Long gameId, Long itemId) {
		LambdaQueryWrapper<GameItemRule> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(GameItemRule::getGameId, gameId);
		queryWrapper.eq(GameItemRule::getItemId, itemId);
		return gameItemRuleMapper.selectOne(queryWrapper);
	}

	public int saveItemRule(GameItemRule gameItemRule) {
		if (Objects.isNull(gameItemRule.getId())) {
			return gameItemRuleMapper.insert(gameItemRule);
		}
		else {
			return gameItemRuleMapper.updateById(gameItemRule);
		}
	}

	public int setItemRuleBatch(Long gameId, List<Long> itemId) {
		// GameItemRule gameRuleSetting = new GameItemRule();
		// gameRuleSetting.setGameId(gameId);
		// gameRuleSetting.setItemId();
		// gameRuleSettingMapper.insert();

		return 1;
	}

	public int setItemRule(Long gameId, Long itemId, Long ruleId) {
		return 1;
	}

	public List<GameItemRuleVo> getItemRule(Long gameId, Long groupId, Long ruleId) {
		return gameItemRuleMapper.selectItemRule(gameId, groupId, ruleId);
	}

}

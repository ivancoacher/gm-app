package com.jsnjwj.facade.manager;

import com.jsnjwj.facade.mapper.GameItemRuleMapper;
import com.jsnjwj.facade.mapper.GameRuleSettingMapper;
import com.jsnjwj.facade.vo.rule.GameItemRuleVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<GameItemRuleVo> getGameItemRule(Long gameId, Long itemId) {



        return null;
    }

    public int setItemRuleBatch(Long gameId, List<Long> itemId) {
//        GameItemRule gameRuleSetting = new GameItemRule();
//        gameRuleSetting.setGameId(gameId);
//        gameRuleSetting.setItemId();
//        gameRuleSettingMapper.insert();

        return 1;
    }

    public int setItemRule(Long gameId, Long itemId, Long ruleId){
        return 1;
    }

}

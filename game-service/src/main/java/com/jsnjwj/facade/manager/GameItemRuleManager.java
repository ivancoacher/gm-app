package com.jsnjwj.facade.manager;

import com.jsnjwj.facade.mapper.GameRuleSettingMapper;
import com.jsnjwj.facade.vo.GameItemRuleVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameItemRuleManager {

    private final GameRuleSettingMapper gameRuleSettingMapper;


    public GameItemRuleVo getGameItemRule(Long gameId) {
        return null;
    }
}

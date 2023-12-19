package com.jsnjwj.facade.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsnjwj.facade.entity.GameRuleSetting;
import com.jsnjwj.facade.entity.GameRuleSettingDetail;
import com.jsnjwj.facade.mapper.GameRuleSettingDetailMapper;
import com.jsnjwj.facade.mapper.GameRuleSettingMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class GameSettingRuleManager {

    @Resource
    private GameRuleSettingMapper gameRuleSettingMapper;

    @Resource
    private GameRuleSettingDetailMapper gameRuleSettingDetailMapper;

    public int saveRuleInfo(GameRuleSetting ruleSetting) {
        return gameRuleSettingMapper.insert(ruleSetting);
    }

    public int updateRuleInfo(GameRuleSetting ruleSetting) {
        return gameRuleSettingMapper.updateById(ruleSetting);
    }

    public int saveRuleDetail(Long gameId, Long itemId, List<GameRuleSettingDetail> list) {
        LambdaQueryWrapper<GameRuleSettingDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GameRuleSettingDetail::getSettingId, itemId);
        gameRuleSettingDetailMapper.delete(wrapper);
        return gameRuleSettingDetailMapper.insertBatchSomeColumn(list);
    }

    public GameRuleSetting fetchRule(Long gameId, Long itemId) {
        LambdaQueryWrapper<GameRuleSetting> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GameRuleSetting::getGameId, gameId);
        wrapper.eq(GameRuleSetting::getItemId, itemId);
        return gameRuleSettingMapper.selectById(wrapper);
    }

    public List<GameRuleSettingDetail> fetchRuleDetail(Long settingId) {
        return new ArrayList<>();
    }

}

package com.jsnjwj.facade.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsnjwj.facade.entity.GameRuleSetting;
import com.jsnjwj.facade.entity.GameRuleSettingDetail;
import com.jsnjwj.facade.mapper.GameRuleSettingDetailMapper;
import com.jsnjwj.facade.mapper.GameRuleSettingMapper;
import com.jsnjwj.facade.vo.GameRuleSettingVo;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

public class GameRuleSettingManager {

    @Resource
    private GameRuleSettingMapper gameRuleSettingMapper;

    @Resource
    private GameRuleSettingDetailMapper gameRuleSettingDetailMapper;

    public GameRuleSettingVo fetchOne(Long gameId, Long itemId) {
        LambdaQueryWrapper<GameRuleSetting> query = new LambdaQueryWrapper<>();
        query.eq(GameRuleSetting::getGameId, gameId);
        query.eq(GameRuleSetting::getItemId, itemId);
        GameRuleSetting gameRuleSetting = gameRuleSettingMapper.selectOne(query);

        GameRuleSettingVo gameRuleSettingVo = new GameRuleSettingVo();

        gameRuleSettingVo.setGameId(gameId);
        gameRuleSettingVo.setItemId(itemId);
        gameRuleSettingVo.setJudgeGroupNum(gameRuleSetting.getJudgeGroupNum());

        LambdaQueryWrapper<GameRuleSettingDetail> detailQuery = new LambdaQueryWrapper<>();
        detailQuery.eq(GameRuleSettingDetail::getSettingId, gameRuleSetting.getId());
        List<GameRuleSettingDetail> details = gameRuleSettingDetailMapper.selectList(detailQuery);
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

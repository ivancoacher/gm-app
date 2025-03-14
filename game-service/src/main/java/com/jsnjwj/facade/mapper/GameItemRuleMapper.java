package com.jsnjwj.facade.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsnjwj.facade.entity.GameItemRule;
import com.jsnjwj.facade.entity.GameRuleSetting;
import com.jsnjwj.facade.vo.rule.GameItemRuleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author user
 * @description 针对表【tc_game_item_rule】的数据库操作Mapper
 * @createDate 2023-07-09 01:36:35
 * @Entity com.jsnjwj.service.entity.TcGameRuleSetting
 */
public interface GameItemRuleMapper extends BaseMapper<GameItemRule> {

	List<GameItemRuleVo> selectItemRule(@Param("gameId") Long gameId, @Param("groupId") Long groupId,
			@Param("itemId") Long itemId);

}

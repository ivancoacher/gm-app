package com.jsnjwj.facade.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsnjwj.facade.entity.GameJudgeEntity;
import com.jsnjwj.facade.vo.GameJudgeVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author user
 * @description 针对表【tc_game_judge】的数据库操作Mapper
 * @createDate 2023-07-09 01:36:35
 * @Entity com.jsnjwj.service.entity.TcGameJudge
 */
public interface GameJudgeMapper extends BaseMapper<GameJudgeEntity> {

	@Select("select gj.id,gj.game_id as gameId,j.judge_name as judgeName,j.phone "
			+ "from tc_game_judge as gj left join tc_judge as j on j.id = gj.judge_id "
			+ "where gj.game_id = #{gameId}")
	List<GameJudgeVo> selectPageList(@Param("gameId") Long gameId);

	@Select("select gj.id,gj.game_id as gameId,j.judge_name as judgeName,j.phone "
			+ "from tc_game_judge as gj left join tc_judge as j on j.id = gj.judge_id "
			+ "where gj.game_id = #{gameId}")
	List<GameJudgeVo> selectItemList(@Param("gameId") Long gameId, @Param("itemId") Long itemId);

	@Select("select gj.id,gj.game_id as gameId,j.judge_name as judgeName,j.phone "
			+ "from tc_game_judge as gj left join tc_judge as j on j.id = gj.judge_id "
			+ "where gj.game_id = #{gameId}")
	List<GameJudgeVo> selectAllList(@Param("gameId") Long gameId);

}

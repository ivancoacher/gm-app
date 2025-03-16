package com.jsnjwj.facade.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsnjwj.facade.entity.GameSessionEntity;
import com.jsnjwj.facade.entity.GameSessionItemEntity;
import com.jsnjwj.facade.vo.session.SessionItemVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author user
 * @description 针对表【tc_game_area】的数据库操作Mapper
 * @createDate 2023-07-09 01:36:35
 * @Entity com.jsnjwj.service.entity.TcGameArea
 */
public interface GameSessionItemMapper extends BaseMapper<GameSessionItemEntity> {

	List<SessionItemVo> fetchBySessionId(@Param("gameId") Long gameId, @Param("sessionId") Long sessionId);

	void saveBatch(@Param("list") List<GameSessionItemEntity> list);

	List<Long> selectArrangedSessionIds(@Param("gameId") Long gameId);
}

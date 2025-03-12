package com.jsnjwj.facade.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsnjwj.facade.dao.SessionDrawDao;
import com.jsnjwj.facade.dao.SessionDrawListDao;
import com.jsnjwj.facade.entity.GameAreaEntity;
import com.jsnjwj.facade.entity.GameDrawEntity;
import com.jsnjwj.facade.entity.GameSessionEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author user
 * @description 针对表【tc_game_area】的数据库操作Mapper
 * @createDate 2023-07-09 01:36:35
 * @Entity com.jsnjwj.service.entity.TcGameArea
 */
public interface GameDrawMapper extends BaseMapper<GameDrawEntity> {
    void saveBatch(@Param("list") List<GameDrawEntity> list);

    List<SessionDrawDao> getBySessionNo(@Param("gameId") Long gameId, @Param("sessionNo") Integer sessionNo);

    List<SessionDrawListDao> getSessionList(@Param("gameId") Long gameId);
}

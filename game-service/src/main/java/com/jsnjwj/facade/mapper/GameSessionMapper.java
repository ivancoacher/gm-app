package com.jsnjwj.facade.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsnjwj.facade.entity.GameSessionEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author user
 * @description 针对表【tc_game_area】的数据库操作Mapper
 * @createDate 2023-07-09 01:36:35
 * @Entity com.jsnjwj.service.entity.TcGameArea
 */
public interface GameSessionMapper extends BaseMapper<GameSessionEntity> {

	void saveBatch(@Param("list") List<GameSessionEntity> list);

}

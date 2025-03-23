package com.jsnjwj.facade.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsnjwj.facade.entity.ArrangeAreaSessionEntity;
import org.apache.ibatis.annotations.Param;

/**
 * @author user
 * @description 针对表【tc_game_area_item】的数据库操作Mapper
 * @createDate 2023-07-09 01:36:35
 * @Entity com.jsnjwj.service.entity.TcGameAreaItem
 */
public interface ArrangeAreaSessionMapper extends BaseMapper<ArrangeAreaSessionEntity> {

    int checkSessionItemExist(@Param("gameId") Long gameId, @Param("sessionId") Long sessionId);

}

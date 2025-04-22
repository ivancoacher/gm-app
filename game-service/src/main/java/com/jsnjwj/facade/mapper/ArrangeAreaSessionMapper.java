package com.jsnjwj.facade.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsnjwj.facade.entity.ArrangeAreaSessionEntity;
import com.jsnjwj.facade.vo.AreaSessionVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author user
 * @description 针对表【tc_game_area_item】的数据库操作Mapper
 * @createDate 2023-07-09 01:36:35
 * @Entity com.jsnjwj.service.entity.TcGameAreaItem
 */
public interface ArrangeAreaSessionMapper extends BaseMapper<ArrangeAreaSessionEntity> {

	int checkSessionItemExist(@Param("gameId") Long gameId, @Param("sessionId") Long sessionId);

	int saveBatch(@Param("list") List<ArrangeAreaSessionEntity> list);

	List<AreaSessionVo> selectSessionByAreaId(@Param("gameId") Long gameId, @Param("areaId") Long areaId);

}

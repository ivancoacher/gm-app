package com.jsnjwj.facade.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsnjwj.facade.entity.TcGameAreaItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author user
 * @description 针对表【tc_game_area_item】的数据库操作Mapper
 * @createDate 2023-07-09 01:36:35
 * @Entity com.jsnjwj.service.entity.TcGameAreaItem
 */
public interface TcGameAreaItemMapper extends BaseMapper<TcGameAreaItem> {

	void saveBatch(@Param("list") List<TcGameAreaItem> list);

}

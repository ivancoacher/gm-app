package com.jsnjwj.facade.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.facade.entity.TcGameItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsnjwj.facade.vo.ItemLabelVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author user
 * @description 针对表【tc_game_item】的数据库操作Mapper
 * @createDate 2023-07-09 01:36:35
 * @Entity com.jsnjwj.service.entity.TcGameItem
 */
public interface TcGameItemMapper extends BaseMapper<TcGameItem> {
    String wrapperSql = "select i.id,i.item_name ,i.group_id ,g.group_name ,i.game_id  " +
            "from tc_game_item as i left join tc_game_group as g on g.id = i.group_id ";
    String sql = "select game_id as gameId,item_name as itemName,group_id as groupId,group_name as groupName,id as itemId from ("+wrapperSql+") as result ${ew.customSqlSegment}";
    @Select(sql)
//            "${ew.customSqlSegment}")
    Page<ItemLabelVo> selectByPage(Page page, @Param("ew")LambdaQueryWrapper lwrapper);
}

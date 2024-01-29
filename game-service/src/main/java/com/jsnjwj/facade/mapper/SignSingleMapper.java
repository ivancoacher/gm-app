package com.jsnjwj.facade.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsnjwj.facade.dto.SignSingleDto;
import com.jsnjwj.facade.entity.SignSingleEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author user
 * @description 针对表【tc_sign_single】的数据库操作Mapper
 * @createDate 2023-07-09 01:36:35
 * @Entity com.jsnjwj.service.entity.TcSignSingle
 */
public interface SignSingleMapper extends BaseMapper<SignSingleEntity> {

	String wrapperSql = "select ss.* ,gg.group_name ,gi.item_name "
			+ " from tc_sign_single as ss left join tc_sign_team as st on st.id = ss.team_id "
			+ "left join tc_game_group as gg on gg.id = ss.group_id "
			+ "left join tc_game_item as gi on gi.id = ss.item_id ";

	String sql = "select game_id as gameId,group_id as groupId,id as applyId,item_id as itemId,"
			+ "group_name as groupName,item_name as itemName,name,age,sex,remark,team_id as teamId,org_name as orgName from ("
			+ wrapperSql + ") as result ${ew.customSqlSegment} limit #{page},#{limit}";

	@Select(sql)
	List<SignSingleDto> selectByPage(@Param("page") Integer page, @Param("limit") Integer limit,
			@Param("ew") LambdaQueryWrapper lwrapper);

	void saveBatch(@Param("list") List<SignSingleEntity> list);

}

package com.jsnjwj.facade.mapper;

import com.jsnjwj.facade.entity.TcGameArea;
import com.jsnjwj.facade.entity.TcSignTeam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author user
 * @description 针对表【tc_sign_team】的数据库操作Mapper
 * @createDate 2023-07-09 01:36:35
 * @Entity com.jsnjwj.service.entity.TcSignTeam
 */
public interface TcSignTeamMapper extends BaseMapper<TcSignTeam> {
    void saveBatch(@Param("list") List<TcSignTeam> list);

}

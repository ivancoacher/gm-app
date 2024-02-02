package com.jsnjwj.facade.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsnjwj.facade.entity.SignOrgEntity;
import com.jsnjwj.facade.entity.SignTeamEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author user
 * @description 针对表【tc_sign_team】的数据库操作Mapper
 * @createDate 2023-07-09 01:36:35
 * @Entity com.jsnjwj.service.entity.TcSignTeam
 */
public interface SignOrgMapper extends BaseMapper<SignOrgEntity> {

}

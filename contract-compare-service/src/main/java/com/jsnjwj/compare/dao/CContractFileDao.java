package com.jsnjwj.compare.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsnjwj.compare.entity.CContractFileEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * (CContractFile)表数据库访问层
 *
 * @author makejava
 * @since 2023-06-23 02:06:13
 */
@Mapper
public interface CContractFileDao extends BaseMapper<CContractFileEntity> {

	int insertOne(CContractFileEntity cContractFileEntity);

}

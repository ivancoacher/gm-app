package com.jsnjwj.compare.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsnjwj.compare.entity.CContractFilePageEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * (CContractFilePage)表数据库访问层
 *
 * @author makejava
 * @since 2023-06-23 02:06:22
 */
@Mapper
public interface CContractFilePageDao extends BaseMapper<CContractFilePageEntity> {

	void insertBatch(List<CContractFilePageEntity> entities);

	@Override
	CContractFilePageEntity selectById(Serializable id);

	int insertOrUpdateBatch(@Param("entities") List<CContractFilePageEntity> entities);

}

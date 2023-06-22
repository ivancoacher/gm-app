package com.jsnjwj.compare.dao;

import com.jsnjwj.compare.entity.CContractFile;
import tk.mybatis.mapper.common.Mapper;

/**
 * (CContractFile)表数据库访问层
 *
 * @author makejava
 * @since 2023-06-23 02:06:13
 */
public interface CContractFileDao extends Mapper<CContractFile> {
    int insertOne(CContractFile cContractFile);

}


package com.jsnjwj.compare.dao;

import com.jsnjwj.compare.entity.CContractFilePage;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * (CContractFilePage)表数据库访问层
 *
 * @author makejava
 * @since 2023-06-23 02:06:22
 */
public interface CContractFilePageDao extends Mapper<CContractFilePage> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    CContractFilePage queryById(Integer id);

    /**
     * 统计总行数
     *
     * @param cContractFilePage 查询条件
     * @return 总行数
     */
    long count(CContractFilePage cContractFilePage);

    /**
     * 新增数据
     *
     * @param cContractFilePage 实例对象
     * @return 影响行数
     */
    int insert(CContractFilePage cContractFilePage);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<CContractFilePage> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<CContractFilePage> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<CContractFilePage> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<CContractFilePage> entities);

    /**
     * 修改数据
     *
     * @param cContractFilePage 实例对象
     * @return 影响行数
     */
    int update(CContractFilePage cContractFilePage);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}


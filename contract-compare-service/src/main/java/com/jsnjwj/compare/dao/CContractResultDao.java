package com.jsnjwj.compare.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsnjwj.compare.entity.CContractResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (CContractResult)表数据库访问层
 *
 * @author makejava
 * @since 2023-06-23 02:06:22
 */
@Mapper
public interface CContractResultDao extends BaseMapper<CContractResult> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    CContractResult queryById(Integer id);

    /**
     * 统计总行数
     *
     * @param cContractResult 查询条件
     * @return 总行数
     */
    long count(CContractResult cContractResult);

    /**
     * 新增数据
     *
     * @param cContractResult 实例对象
     * @return 影响行数
     */
    int insert(CContractResult cContractResult);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<CContractResult> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<CContractResult> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<CContractResult> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<CContractResult> entities);

    /**
     * 修改数据
     *
     * @param cContractResult 实例对象
     * @return 影响行数
     */
    int update(CContractResult cContractResult);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}


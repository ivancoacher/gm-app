package com.jsnjwj.compare.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsnjwj.compare.entity.CContractRecord;
import com.jsnjwj.compare.response.CompareAnalysisChartResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (CContractRecord)表数据库访问层
 *
 * @author makejava
 * @since 2023-06-23 02:06:22
 */
@Mapper
public interface CContractRecordDao extends BaseMapper<CContractRecord> {

	int insert(CContractRecord cContractRecord);

	List<CompareAnalysisChartResponse> selectGroupData(@Param("startTime") String startTime,
			@Param("endTime") String endTime);

}

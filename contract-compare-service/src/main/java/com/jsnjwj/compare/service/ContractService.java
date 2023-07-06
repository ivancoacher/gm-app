package com.jsnjwj.compare.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.compare.entity.CContractFilePageEntity;
import com.jsnjwj.compare.entity.CContractRecordEntity;
import com.jsnjwj.compare.query.*;
import com.jsnjwj.compare.response.CompareAnalysisChartResponse;
import com.jsnjwj.compare.response.CompareAnalysisResponse;
import com.jsnjwj.compare.vo.CompareInfoVo;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;

public interface ContractService {

	ApiResponse compare(ContractCompareQuery query, MultipartFile sourceFile, MultipartFile compareFile)
			throws Exception;

	ApiResponse<Page<CompareInfoVo>> queryList(ContractListQuery query);

	ApiResponse<CContractRecordEntity> queryDetail(ContractDetailQuery query);

	ApiResponse<CContractFilePageEntity> queryResult(CompareResultQuery query);

	ApiResponse<List<CContractFilePageEntity>> queryPages(ComparePagesQuery query);

	ApiResponse<CompareAnalysisResponse> queryAnalysis(Long userId);

	ApiResponse<List<CompareAnalysisChartResponse>> queryAnalysisChart(CompareAnalysisQuery query)
			throws ParseException;

}

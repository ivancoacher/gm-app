package com.jsnjwj.api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.api.aspect.MethodLog;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.compare.entity.CContractFilePage;
import com.jsnjwj.compare.entity.CContractRecord;
import com.jsnjwj.compare.query.*;
import com.jsnjwj.compare.response.CompareAnalysisChartResponse;
import com.jsnjwj.compare.response.CompareAnalysisResponse;
import com.jsnjwj.compare.service.ContractService;
import com.jsnjwj.user.enums.OperateTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/compare")
public class CompareController {

	@Resource
	private ContractService contractService;

	@MethodLog(operType = OperateTypeEnum.DOC_COMPARE, targetId = "", remark = "")
	@PostMapping(value = "/do", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ApiResponse compare(ContractCompareQuery query, HttpServletRequest request) throws Exception {
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
		MultipartFile sourceFile = multiRequest.getFile("sourceFile");
		MultipartFile compareFile = multiRequest.getFile("compareFile");
		query.setUserId(Integer.valueOf((String) request.getAttribute("identifyId")));
		return contractService.compare(query, sourceFile, compareFile);
	}

	@RequestMapping(value = "/list")
	public ApiResponse<Page<CContractRecord>> list(ContractListQuery query, HttpServletRequest request) {
		query.setUserId(Integer.valueOf((String) request.getAttribute("identifyId")));
		return contractService.queryList(query);
	}

	@MethodLog(operType = OperateTypeEnum.VIEW_COMPARE_RESULT, targetId = "", remark = "")
	@GetMapping(value = "/detail", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ApiResponse<CContractRecord> detail(ContractDetailQuery query, HttpServletRequest request) {
		query.setUserId(Integer.valueOf((String) request.getAttribute("identifyId")));
		return contractService.queryDetail(query);
	}

	@GetMapping(value = "/result", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ApiResponse<CContractFilePage> result(CompareResultQuery query, HttpServletRequest request) {
		query.setUserId(Integer.valueOf((String) request.getAttribute("identifyId")));
		return contractService.queryResult(query);
	}

	@GetMapping(value = "/download", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ApiResponse<CContractFilePage> download(CompareResultQuery query, HttpServletRequest request) {
		query.setUserId(Integer.valueOf((String) request.getAttribute("identifyId")));
		return contractService.queryResult(query);
	}

	@GetMapping(value = "/pages", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ApiResponse<List<CContractFilePage>> result(ComparePagesQuery query, HttpServletRequest request) {
		query.setUserId(Integer.valueOf((String) request.getAttribute("identifyId")));
		return contractService.queryPages(query);
	}

	@GetMapping(value = "/analysis", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ApiResponse<CompareAnalysisResponse> analysis(ComparePagesQuery query, HttpServletRequest request) {
		query.setUserId(Integer.valueOf((String) request.getAttribute("identifyId")));
		return contractService.queryAnalysis(Long.valueOf(query.getUserId()));
	}

	@GetMapping(value = "/analysis/chart", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ApiResponse<List<CompareAnalysisChartResponse>> analysis(CompareAnalysisQuery query,
			HttpServletRequest request) throws ParseException {
		query.setUserId(Integer.valueOf((String) request.getAttribute("identifyId")));
		return contractService.queryAnalysisChart(query);
	}

}

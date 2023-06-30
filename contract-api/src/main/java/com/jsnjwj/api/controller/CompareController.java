package com.jsnjwj.api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.compare.entity.CContractFilePage;
import com.jsnjwj.compare.entity.CContractRecord;
import com.jsnjwj.compare.query.ComparePagesQuery;
import com.jsnjwj.compare.query.CompareResultQuery;
import com.jsnjwj.compare.query.ContractDetailQuery;
import com.jsnjwj.compare.query.ContractListQuery;
import com.jsnjwj.compare.service.ContractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/compare")
public class CompareController {

	@Resource
	private ContractService contractService;

	@PostMapping(value = "/do", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ApiResponse compare(HttpServletRequest request) throws Exception {
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
		MultipartFile sourceFile = multiRequest.getFile("sourceFile");
		MultipartFile compareFile = multiRequest.getFile("compareFile");
		return contractService.compare(sourceFile, compareFile);
	}

	@RequestMapping(value = "/list")
	public ApiResponse<Page<CContractRecord>> list(ContractListQuery query, HttpServletRequest request) {
		query.setUserId(Integer.valueOf((String) request.getAttribute("identifyId")));
		return contractService.queryList(query);
	}

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

	@GetMapping(value = "/pages", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ApiResponse<List<CContractFilePage>> result(ComparePagesQuery query, HttpServletRequest request) {
		query.setUserId(Integer.valueOf((String) request.getAttribute("identifyId")));
		return contractService.queryPages(query);
	}

}

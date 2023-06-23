package com.jsnjwj.api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.compare.entity.CContractFilePage;
import com.jsnjwj.compare.entity.CContractRecord;
import com.jsnjwj.compare.query.CompareResultQuery;
import com.jsnjwj.compare.query.ContractDetailQuery;
import com.jsnjwj.compare.query.ContractListQuery;
import com.jsnjwj.compare.response.ApiResponse;
import com.jsnjwj.compare.service.ContractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping(value = "/compare")
public class CompareController {
    @Resource
    private ContractService contractService;

    @PostMapping(value = "/do", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Integer compare(HttpServletRequest request) throws Exception {

        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;

        MultipartFile sourceFile = multiRequest.getFile("sourceFile");
        MultipartFile compareFile = multiRequest.getFile("compareFile");
        contractService.compare(sourceFile, compareFile);
        return 1;
    }

    @GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ApiResponse<Page<CContractRecord>> list(ContractListQuery query) {
        return contractService.queryList(query);
    }


    @GetMapping(value = "/detail", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ApiResponse<CContractRecord> detail(ContractDetailQuery query) {
        return contractService.queryDetail(query);
    }

    @GetMapping(value = "/result", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ApiResponse<CContractFilePage> result(CompareResultQuery query) {
        return contractService.queryResult(query);
    }

}

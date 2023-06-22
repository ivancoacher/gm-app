package com.jsnjwj.api.controller;

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
}

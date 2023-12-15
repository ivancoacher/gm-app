package com.jsnjwj.api.controller;

import com.jsnjwj.common.request.BaseRequest;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.common.utils.ThreadLocalUtil;
import com.jsnjwj.facade.query.SignSingleListQuery;
import com.jsnjwj.facade.query.SignTeamListQuery;
import com.jsnjwj.facade.service.SignApplyService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/game/sign")
public class GameSignController {

    @Resource
    private SignApplyService signApplyService;

    @RequestMapping("/single/page")
    public ApiResponse<?> fetchSinglePage(SignSingleListQuery query) {
        return signApplyService.fetchSinglePage(query);
    }

    @RequestMapping("/single/import")
    public ApiResponse<?> singleImport(BaseRequest query, HttpServletRequest request) throws Exception {
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        MultipartFile sourceFile = multiRequest.getFile("file");
        query.setUserId(ThreadLocalUtil.getCurrentUserId());
        return signApplyService.importSingle(query, sourceFile);
    }

    @RequestMapping("/single/demo/import")
    public ApiResponse<?> singleDemoImport(BaseRequest query, HttpServletRequest request) throws Exception {
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        MultipartFile sourceFile = multiRequest.getFile("file");
        query.setUserId(ThreadLocalUtil.getCurrentUserId());
        return signApplyService.exportSingleDemo(query, sourceFile);
    }

    @RequestMapping("/team/page")
    public ApiResponse<?> fetchTeamPage(SignTeamListQuery query) {
        return signApplyService.fetchTeamPage(query);
    }

    @RequestMapping("/team/import")
    public ApiResponse<?> teamImport(BaseRequest query, HttpServletRequest request) throws Exception {
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        MultipartFile sourceFile = multiRequest.getFile("file");
        query.setUserId(ThreadLocalUtil.getCurrentUserId());
        return signApplyService.importTeam(query, sourceFile);
    }

    @RequestMapping("/team/demo/export")
    public ApiResponse<?> teamDemoExport(BaseRequest query, HttpServletRequest request) throws Exception {
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        MultipartFile sourceFile = multiRequest.getFile("file");
        query.setUserId(ThreadLocalUtil.getCurrentUserId());
        return signApplyService.exportTeamDemo(query, sourceFile);
    }
}

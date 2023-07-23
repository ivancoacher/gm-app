package com.jsnjwj.api.controller;

import com.jsnjwj.common.request.BaseRequest;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.query.SignSingleListQuery;
import com.jsnjwj.facade.query.SignTeamListQuery;
import com.jsnjwj.facade.service.SignApplyService;
import com.jsnjwj.user.entity.UserAccount;
import com.jsnjwj.user.request.LoginRequest;
import com.jsnjwj.user.service.AccountService;
import com.jsnjwj.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/game/sign")
public class GameSignController {
    @Resource
    private SignApplyService signApplyService;

    @RequestMapping("/single/page")
    public ApiResponse<?> fetchSinglePage(SignSingleListQuery query){
        return signApplyService.fetchSinglePage(query);
    }

    @RequestMapping("/team/page")
    public ApiResponse<?> fetchTeamPage(SignTeamListQuery query){
        return signApplyService.fetchTeamPage(query);
    }

    @RequestMapping("/team/import")
    public ApiResponse<?> compare(BaseRequest query, HttpServletRequest request) throws Exception {
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        MultipartFile sourceFile = multiRequest.getFile("file");
        query.setUserId(Integer.valueOf((String) request.getAttribute("identifyId")));
        return signApplyService.importTeam(query, sourceFile);
    }
}

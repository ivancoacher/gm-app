package com.jsnjwj.api.controller;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.query.SignSingleListQuery;
import com.jsnjwj.facade.service.SignApplyService;
import com.jsnjwj.user.entity.UserAccount;
import com.jsnjwj.user.request.LoginRequest;
import com.jsnjwj.user.service.AccountService;
import com.jsnjwj.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
}

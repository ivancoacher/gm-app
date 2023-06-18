package com.jsnjwj.www.controller;

import com.jsnjwj.www.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Slf4j
@Controller
public class OrganizationController {
    @Resource
    private UserService userService;

    @ResponseBody
    @RequestMapping("/")
    public Integer index() {
        return userService.save();
    }
}

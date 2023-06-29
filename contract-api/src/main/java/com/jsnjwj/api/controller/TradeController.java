package com.jsnjwj.api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.trade.entity.TradeInfo;
import com.jsnjwj.trade.request.QueryListRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.management.Query;

@Slf4j
@RestController
public class TradeController {
    @RequestMapping("/list")
    @ResponseBody
    public ApiResponse<Page<TradeInfo>> info(QueryListRequest query) {
        return ApiResponse.success();
    }
}

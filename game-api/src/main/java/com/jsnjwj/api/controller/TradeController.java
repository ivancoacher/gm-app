package com.jsnjwj.api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.common.utils.ThreadLocalUtil;
import com.jsnjwj.trade.entity.TradeLog;
import com.jsnjwj.trade.request.QueryListRequest;
import com.jsnjwj.trade.service.TradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/trade")
public class TradeController {

    @Resource
    private TradeService tradeService;

    @RequestMapping("/list")
    @ResponseBody
    public ApiResponse<Page<TradeLog>> info(QueryListRequest query, HttpServletRequest request) {
        query.setUserId(ThreadLocalUtil.getCurrentUserId());
        return tradeService.fetchList(query);
    }

}

package com.jsnjwj.trade.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.trade.entity.TradeLog;
import com.jsnjwj.trade.request.QueryListRequest;

public interface TradeService {

	ApiResponse<Page<TradeLog>> fetchList(QueryListRequest query);

	int saveLog(TradeLog tradeLog);

}

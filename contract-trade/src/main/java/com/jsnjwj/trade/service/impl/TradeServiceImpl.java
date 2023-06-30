package com.jsnjwj.trade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.trade.dao.TradeLogDao;
import com.jsnjwj.trade.entity.TradeLog;
import com.jsnjwj.trade.request.QueryListRequest;
import com.jsnjwj.trade.service.TradeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TradeServiceImpl implements TradeService {

	@Resource
	private TradeLogDao tradeLogDao;

	@Override
	public ApiResponse<Page<TradeLog>> fetchList(QueryListRequest query) {
		ApiResponse<Page<TradeLog>> response = new ApiResponse<>();

		Page<TradeLog> resultPage = new Page<>();
		resultPage.setCurrent(query.getPageIndex());
		resultPage.setSize(query.getPageSize());
		QueryWrapper<TradeLog> wrapper = new QueryWrapper<>();
		wrapper.lambda().eq(TradeLog::getUserId, query.getUserId());
		if (StringUtils.isNotEmpty(query.getStartTime())) {
			wrapper.lambda().ge(TradeLog::getCreateTime, query.getStartTime());
		}
		if (StringUtils.isNotEmpty(query.getEndTime())) {
			wrapper.lambda().le(TradeLog::getCreateTime, query.getEndTime());
		}

		if (StringUtils.isNotEmpty(query.getKey())) {
			wrapper.lambda()
				.and(wp -> wp.like(TradeLog::getTradeNo, query.getKey())
					.or()
					.like(TradeLog::getTradeContent, query.getKey()));
		}
		resultPage = tradeLogDao.selectPage(resultPage, wrapper);

		response.setCode(20000);
		response.setMessage("查询成功");
		response.setData(resultPage);
		return response;
	}

	@Override
	public int saveLog(TradeLog tradeLog) {
		return tradeLogDao.insert(tradeLog);
	}

}

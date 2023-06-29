package com.jsnjwj.trade.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsnjwj.trade.entity.TradeLog;

public interface TradeInfoDao extends BaseMapper<TradeLog> {
    int insertOne(TradeInfoDao tradeInfo);

}
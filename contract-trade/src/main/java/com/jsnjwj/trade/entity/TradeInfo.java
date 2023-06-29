package com.jsnjwj.trade.entity;

import lombok.Data;

import java.time.LocalTime;

@Data
public class TradeInfo {
    private String tradeNo;

    private String tradeName;

    private String tradeType;

    private LocalTime createTime;

    private LocalTime updateTime;



}

package com.jsnjwj.trade.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;

@Data
@TableName("c_trade_log")
public class TradeLog implements Serializable {

	private static final long serialVersionUID = 748741055830395712L;

	private String tradeNo;

	private Long userId;

	private BigDecimal tradeAmount;

	private String tradeType;

	private String tradeContent;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;

}

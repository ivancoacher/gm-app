package com.jsnjwj.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("c_user_account")
public class UserAccount {

	private Long id;

	private Long userId;

	private BigDecimal totalRecharge;

	private BigDecimal totalExpense;

	private BigDecimal balance;

	private Date createTime;

	private Date updateTime;

}

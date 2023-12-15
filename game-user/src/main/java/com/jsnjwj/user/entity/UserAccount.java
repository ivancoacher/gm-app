package com.jsnjwj.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("c_user_account")
public class UserAccount {

    private Long id;

    private Long userId;

    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal totalRecharge;

    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal totalExpense;

    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal balance;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}

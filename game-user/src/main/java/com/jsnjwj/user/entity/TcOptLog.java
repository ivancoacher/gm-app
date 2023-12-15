package com.jsnjwj.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("tc_opt_log")
public class TcOptLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String remark;

    private Integer operateType;

    private Long targetId;

    private Date createTime;

    private Date updateTime;

    ;

}

package com.jsnjwj.user.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class OperateLogVo {

    private Long id;

    private Long userId;

    private String username;

    private Integer operateType;

    private String operateTypeDesc;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private String remark;

}

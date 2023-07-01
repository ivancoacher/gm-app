package com.jsnjwj.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jsnjwj.user.enums.OperateTypeEnum;
import lombok.Data;

import java.util.Date;

/**
 * (CContractFile)实体类
 *
 * @author makejava
 * @since 2023-06-23 02:06:19
 */
@Data
@TableName("c_opt_log")
public class OptLog {

	private Long userId;

	private OperateTypeEnum operateType;

	private String remark;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

}

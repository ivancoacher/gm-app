package com.jsnjwj.compare.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jsnjwj.compare.enums.CompareStateEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * (CContractRecord)实体类
 *
 * @author makejava
 * @since 2023-06-23 02:06:22
 */
@Data
@TableName("c_contract_record")
public class CContractRecordEntity implements Serializable {

	private static final long serialVersionUID = 748741055830395712L;

	@TableId(type = IdType.AUTO)
	private Integer id;

	private Integer userId;

	private String tradeNo;

	private String operateDay;

	private Integer compareState;

	/**
	 * 原文档编号
	 */
	private Long originFileId;

	private String originFileName;

	/**
	 * 对比文档编号
	 */
	private Long compareFileId;

	private String compareFileName;

	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
	private Date createTime;

	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
	private Date updateTime;

}

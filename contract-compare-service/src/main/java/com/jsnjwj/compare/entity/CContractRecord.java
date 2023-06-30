package com.jsnjwj.compare.entity;

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
public class CContractRecord implements Serializable {

	private static final long serialVersionUID = 748741055830395712L;

	private Integer id;

	private Integer userId;

	private CompareStateEnum compareState;

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

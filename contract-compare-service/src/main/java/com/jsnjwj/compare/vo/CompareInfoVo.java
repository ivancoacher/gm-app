package com.jsnjwj.compare.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 合同比对数据实体
 */
@Data
public class CompareInfoVo {

	private Long compareId;

	private String tradeNo;

	private Long originalFileId;

	private String originalFileName;

	private Long compareFileId;

	private String compareFileName;

	private Integer compareState;

	private String compareStateDesc;

	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
	private Date updateTime;

}

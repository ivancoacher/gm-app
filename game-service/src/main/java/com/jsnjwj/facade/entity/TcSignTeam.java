package com.jsnjwj.facade.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName tc_sign_team
 */
@Data
@TableName(value = "tc_sign_team")

@AllArgsConstructor
@NoArgsConstructor
public class TcSignTeam implements Serializable {

	/**
	 *
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 *
	 */
	private Long gameId;

	/**
	 *
	 */
	@ExcelProperty("队伍名称")
	private String teamName;

	/**
	 *
	 */
	@ExcelProperty("领队名")
	private String leaderName;

	/**
	 *
	 */
	@ExcelProperty("领队电话")
	private String leaderTel;

	/**
	 *
	 */
	private String coachName;

	/**
	 *
	 */
	private String coachTel;

	/**
	 *
	 */
	private String remark;

	/**
	 *
	 */
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/**
	 *
	 */
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
	private Date updateTime;

	/**
	 *
	 */
	private Integer signStatus;

}
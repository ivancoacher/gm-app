package com.jsnjwj.facade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName tc_sign_group
 */
@Data
@TableName(value = "tc_sign_group")
public class SignGroupEntity implements Serializable {

	/**
	 *
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 赛事编号
	 *
	 */
	private Integer gameId;

	/**
	 * 组别编号
	 *
	 */
	private Integer groupId;

	/**
	 * 项目编号
	 */
	private Integer itemId;

	/**
	 * 报名编号
	 */
	private Integer signId;

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

}
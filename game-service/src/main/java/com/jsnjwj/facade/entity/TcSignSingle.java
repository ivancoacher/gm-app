package com.jsnjwj.facade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName tc_sign_single
 */
@Data
@TableName(value = "tc_sign_single")
public class TcSignSingle implements Serializable {

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
	private Long groupId;

	/**
	 *
	 */
	private Long itemId;

	/**
	 *
	 */
	private Integer teamId;

	/**
	 *
	 */
	private String name;

	/**
	 *
	 */
	private String teamName;

	/**
	 *
	 */
	private Integer sex;

	/**
	 *
	 */
	private Integer age;

	/**
	 *
	 */
	private String remark;

	/**
	 *
	 */
	private Integer signStatus;

	/**
	 *
	 */
	private Integer auditStatus;

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
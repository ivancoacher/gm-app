package com.jsnjwj.facade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
public class SignSingleEntity implements Serializable {

	/**
	 *
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

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
	private Long teamId;

	/**
	 *
	 */
	private String name;

	private Long orgId;

	/**
	 * 单位
	 */
	private String orgName;

	private String country;

	private String nation;

	private String cardNum;

	private String studentNum;

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
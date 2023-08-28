package com.jsnjwj.facade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName tc_game_rule_setting
 */
@TableName(value = "tc_game_rule_setting")
@Data
public class SignArrangeRecordEntity implements Serializable {

	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 设置编号
	 */
	private Long settingId;

	/**
	 * 报名信息编号
	 */
	private Long signId;

	/**
	 * 排序
	 */
	private Integer sort;

	/**
	 *
	 */
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
	private Date createdAt;

	/**
	 *
	 */
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
	private Date updatedAt;

}
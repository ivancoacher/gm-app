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
 * @TableName tc_game_rule_setting_detail
 */
@TableName(value = "tc_game_rule_setting_detail")
@Data
public class TcGameRuleSettingDetail implements Serializable {

	/**
	 *
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 *
	 */
	private Integer settingId;

	/**
	 *
	 */
	private Integer num;

	/**
	 *
	 */
	private Integer scoreRatio;

	/**
	 *
	 */
	private Integer scoreWeight;

	/**
	 *
	 */
	private Integer extraType;

	/**
	 *
	 */
	private String extraName;

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
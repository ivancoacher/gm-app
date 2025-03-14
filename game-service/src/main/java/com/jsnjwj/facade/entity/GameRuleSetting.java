package com.jsnjwj.facade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jsnjwj.facade.enums.SettingRuleEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName tc_game_rule_setting
 */
@TableName(value = "tc_game_rule_setting")
@Data
public class GameRuleSetting implements Serializable {

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
	private Long itemId;

	/**
	 *
	 */
	private Integer judgeGroupNum;

	/**
	 * 积分规则
	 */
	private SettingRuleEnum scoreRule;

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
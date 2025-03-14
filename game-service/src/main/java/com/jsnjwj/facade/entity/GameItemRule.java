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
 * @TableName tc_game_item_rule
 */
@TableName(value = "tc_game_item_rule")
@Data
public class GameItemRule implements Serializable {

	/**
	 *
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 赛事id
	 */
	private Long gameId;

	/**
	 * 项目id
	 */
	private Long itemId;

	/**
	 * 规则编号
	 */
	private Integer ruleId;

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
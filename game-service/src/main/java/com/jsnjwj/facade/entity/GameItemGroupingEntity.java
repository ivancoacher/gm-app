package com.jsnjwj.facade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName tc_game_item_grouping
 */
@Data
@TableName(value = "tc_game_item_grouping")
public class GameItemGroupingEntity implements Serializable {

	/**
	 *
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 *
	 */
	private Integer gameId;

	/**
	 *
	 */
	private Integer itemId;

	/**
	 *
	 */
	private String signId;

	/**
	 *
	 */
	private Integer sort;

	/**
	 *
	 */
	private Integer creatorId;

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
	 * 1：个人报名 2：团队报名
	 */
	private Integer signType;

}
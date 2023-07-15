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
 * @TableName tc_games_ext
 */
@Data
@TableName(value = "tc_games_ext")
public class TcGamesExt implements Serializable {

	/**
	 *
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 *
	 */
	private String gameName;

	/**
	 *
	 */
	private String gameType;

	/**
	 *
	 */
	private String signType;

	/**
	 *
	 */
	private Date startTime;

	/**
	 *
	 */
	private Date endTime;

	/**
	 *
	 */
	private Date fightStartTime;

	/**
	 *
	 */
	private Integer status;

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
	private String smallTitle;

	/**
	 *
	 */
	private Integer applyCount;

	/**
	 *
	 */
	private Date fightEndTime;

}
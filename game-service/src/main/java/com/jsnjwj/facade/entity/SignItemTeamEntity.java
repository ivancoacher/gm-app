package com.jsnjwj.facade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName tc_game_group
 */
@TableName(value = "tc_sign_item_team")
@Data
public class SignItemTeamEntity implements Serializable {

	/**
	 *
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 *
	 */
	private Long gameId;

	private Long groupId;

	private Long itemId;

	private Long teamId;

}
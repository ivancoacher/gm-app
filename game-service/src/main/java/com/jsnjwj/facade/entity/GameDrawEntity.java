package com.jsnjwj.facade.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName tc_game_area
 */
@Data
@TableName(value = "tc_game_draw")
public class GameDrawEntity extends BaseEntity {

	/**
	 * 赛事id
	 */
	private Long gameId;

	private Long itemId;

	/**
	 * 场次名称
	 */
	private Long sessionId;

	/**
	 * 状态
	 */
	private Integer sessionNo;

	/**
	 * 报名id
	 */
	private Long signId = 0L;

	private Long teamId = 0L;

	private Integer sort;

	private Integer drawType = 0;

}
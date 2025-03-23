package com.jsnjwj.facade.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName tc_game_area
 */
@Data
@TableName(value = "tc_game_session")
public class GameSessionEntity extends BaseEntity {

	/**
	 * 赛事id
	 */
	private Long gameId;

	/**
	 * 场次名称
	 */
	private String sessionName;

	/**
	 * 状态
	 */
	private Integer status;

	/**
	 *
	 */
	private Integer sessionNo;

}
package com.jsnjwj.facade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * @TableName tc_game_judge
 */
@TableName(value = "tc_game_judge")
public class TcGameJudge implements Serializable {

	/**
	 *
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 *
	 */
	private String judgeId;

	/**
	 *
	 */
	private String gameId;

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	public Integer getId() {
		return id;
	}

	/**
	 *
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 *
	 */
	public String getJudgeId() {
		return judgeId;
	}

	/**
	 *
	 */
	public void setJudgeId(String judgeId) {
		this.judgeId = judgeId;
	}

	/**
	 *
	 */
	public String getGameId() {
		return gameId;
	}

	/**
	 *
	 */
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

}
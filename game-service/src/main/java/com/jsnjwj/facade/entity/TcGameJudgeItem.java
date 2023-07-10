package com.jsnjwj.facade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * @TableName tc_game_judge_item
 */
@TableName(value = "tc_game_judge_item")
public class TcGameJudgeItem implements Serializable {

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
	private Integer judgeId;

	/**
	 *
	 */
	private Integer itemId;

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
	public Integer getGameId() {
		return gameId;
	}

	/**
	 *
	 */
	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}

	/**
	 *
	 */
	public Integer getJudgeId() {
		return judgeId;
	}

	/**
	 *
	 */
	public void setJudgeId(Integer judgeId) {
		this.judgeId = judgeId;
	}

	/**
	 *
	 */
	public Integer getItemId() {
		return itemId;
	}

	/**
	 *
	 */
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

}
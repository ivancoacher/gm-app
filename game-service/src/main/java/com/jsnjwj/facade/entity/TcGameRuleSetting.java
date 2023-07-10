package com.jsnjwj.facade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * @TableName tc_game_rule_setting
 */
@TableName(value = "tc_game_rule_setting")
public class TcGameRuleSetting implements Serializable {

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
	private Integer judgeGroupNum;

	/**
	 *
	 */
	private Integer scoreRule;

	/**
	 *
	 */
	private Date createdAt;

	/**
	 *
	 */
	private Date updatedAt;

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
	public Integer getItemId() {
		return itemId;
	}

	/**
	 *
	 */
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	/**
	 *
	 */
	public Integer getJudgeGroupNum() {
		return judgeGroupNum;
	}

	/**
	 *
	 */
	public void setJudgeGroupNum(Integer judgeGroupNum) {
		this.judgeGroupNum = judgeGroupNum;
	}

	/**
	 *
	 */
	public Integer getScoreRule() {
		return scoreRule;
	}

	/**
	 *
	 */
	public void setScoreRule(Integer scoreRule) {
		this.scoreRule = scoreRule;
	}

	/**
	 *
	 */
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 *
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 *
	 */
	public Date getUpdatedAt() {
		return updatedAt;
	}

	/**
	 *
	 */
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

}
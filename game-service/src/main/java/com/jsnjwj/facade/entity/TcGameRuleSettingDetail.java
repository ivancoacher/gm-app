package com.jsnjwj.facade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * @TableName tc_game_rule_setting_detail
 */
@TableName(value = "tc_game_rule_setting_detail")
public class TcGameRuleSettingDetail implements Serializable {

	/**
	 *
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 *
	 */
	private Integer settingId;

	/**
	 *
	 */
	private Integer num;

	/**
	 *
	 */
	private Integer scoreRatio;

	/**
	 *
	 */
	private Integer scoreWeight;

	/**
	 *
	 */
	private Integer extraType;

	/**
	 *
	 */
	private String extraName;

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
	public Integer getSettingId() {
		return settingId;
	}

	/**
	 *
	 */
	public void setSettingId(Integer settingId) {
		this.settingId = settingId;
	}

	/**
	 *
	 */
	public Integer getNum() {
		return num;
	}

	/**
	 *
	 */
	public void setNum(Integer num) {
		this.num = num;
	}

	/**
	 *
	 */
	public Integer getScoreRatio() {
		return scoreRatio;
	}

	/**
	 *
	 */
	public void setScoreRatio(Integer scoreRatio) {
		this.scoreRatio = scoreRatio;
	}

	/**
	 *
	 */
	public Integer getScoreWeight() {
		return scoreWeight;
	}

	/**
	 *
	 */
	public void setScoreWeight(Integer scoreWeight) {
		this.scoreWeight = scoreWeight;
	}

	/**
	 *
	 */
	public Integer getExtraType() {
		return extraType;
	}

	/**
	 *
	 */
	public void setExtraType(Integer extraType) {
		this.extraType = extraType;
	}

	/**
	 *
	 */
	public String getExtraName() {
		return extraName;
	}

	/**
	 *
	 */
	public void setExtraName(String extraName) {
		this.extraName = extraName;
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
package com.jsnjwj.facade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * @TableName tc_sign_group
 */
@TableName(value = "tc_sign_group")
public class TcSignGroup implements Serializable {

	/**
	 *
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 赛事编号
	 *
	 */
	private Integer gameId;

	/**
	 * 组别编号
	 *
	 */
	private Integer groupId;

	/**
	 * 项目编号
	 */
	private Integer itemId;

	/**
	 * 报名编号
	 */
	private Integer signId;

	/**
	 *
	 */
	private Date createTime;

	/**
	 *
	 */
	private Date updateTime;

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
	 * 赛事编号
	 *
	 */
	public Integer getGameId() {
		return gameId;
	}

	/**
	 * 赛事编号
	 *
	 */
	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}

	/**
	 * 组别编号
	 *
	 */
	public Integer getGroupId() {
		return groupId;
	}

	/**
	 * 组别编号
	 *
	 */
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	/**
	 * 项目编号
	 */
	public Integer getItemId() {
		return itemId;
	}

	/**
	 * 项目编号
	 */
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	/**
	 * 报名编号
	 */
	public Integer getSignId() {
		return signId;
	}

	/**
	 * 报名编号
	 */
	public void setSignId(Integer signId) {
		this.signId = signId;
	}

	/**
	 *
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 *
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 *
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 *
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
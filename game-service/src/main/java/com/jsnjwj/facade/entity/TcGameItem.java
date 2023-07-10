package com.jsnjwj.facade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * @TableName tc_game_item
 */
@TableName(value = "tc_game_item")
public class TcGameItem implements Serializable {

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
	private Integer groupId;

	/**
	 *
	 */
	private String itemName;

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
	public Integer getGroupId() {
		return groupId;
	}

	/**
	 *
	 */
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	/**
	 *
	 */
	public String getItemName() {
		return itemName;
	}

	/**
	 *
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	/**
	 *
	 */
	public Integer getSort() {
		return sort;
	}

	/**
	 *
	 */
	public void setSort(Integer sort) {
		this.sort = sort;
	}

	/**
	 *
	 */
	public Integer getCreatorId() {
		return creatorId;
	}

	/**
	 *
	 */
	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
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
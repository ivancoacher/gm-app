package com.jsnjwj.facade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * @TableName tc_game_item_grouping
 */
@TableName(value = "tc_game_item_grouping")
public class TcGameItemGrouping implements Serializable {

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
	private String signId;

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

	/**
	 * 1：个人报名 2：团队报名
	 */
	private Integer signType;

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
	public String getSignId() {
		return signId;
	}

	/**
	 *
	 */
	public void setSignId(String signId) {
		this.signId = signId;
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

	/**
	 * 1：个人报名 2：团队报名
	 */
	public Integer getSignType() {
		return signType;
	}

	/**
	 * 1：个人报名 2：团队报名
	 */
	public void setSignType(Integer signType) {
		this.signType = signType;
	}

}
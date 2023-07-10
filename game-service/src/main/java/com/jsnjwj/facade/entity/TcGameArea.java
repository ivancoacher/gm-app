package com.jsnjwj.facade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * @TableName tc_game_area
 */
@TableName(value = "tc_game_area")
public class TcGameArea implements Serializable {

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
	private String areaName;

	/**
	 *
	 */
	private Integer status;

	/**
	 *
	 */
	private Integer areaNo;

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
	public String getAreaName() {
		return areaName;
	}

	/**
	 *
	 */
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	/**
	 *
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 *
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 *
	 */
	public Integer getAreaNo() {
		return areaNo;
	}

	/**
	 *
	 */
	public void setAreaNo(Integer areaNo) {
		this.areaNo = areaNo;
	}

}
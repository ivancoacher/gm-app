package com.jsnjwj.facade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * @TableName tc_game_group_sign_ext
 */
@TableName(value = "tc_game_group_sign_ext")
public class TcGameGroupSignExt implements Serializable {

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
	private String dictCode;

	/**
	 *
	 */
	private String singValue;

	/**
	 *
	 */
	private Integer singForce;

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
	public String getDictCode() {
		return dictCode;
	}

	/**
	 *
	 */
	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}

	/**
	 *
	 */
	public String getSingValue() {
		return singValue;
	}

	/**
	 *
	 */
	public void setSingValue(String singValue) {
		this.singValue = singValue;
	}

	/**
	 *
	 */
	public Integer getSingForce() {
		return singForce;
	}

	/**
	 *
	 */
	public void setSingForce(Integer singForce) {
		this.singForce = singForce;
	}

}
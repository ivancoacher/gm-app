package com.jsnjwj.facade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * @TableName tc_games_ext
 */
@TableName(value = "tc_games_ext")
public class TcGamesExt implements Serializable {

	/**
	 *
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 *
	 */
	private String gameName;

	/**
	 *
	 */
	private String gameType;

	/**
	 *
	 */
	private String signType;

	/**
	 *
	 */
	private Date startTime;

	/**
	 *
	 */
	private Date endTime;

	/**
	 *
	 */
	private Date fightStartTime;

	/**
	 *
	 */
	private Integer status;

	/**
	 *
	 */
	private Date createTime;

	/**
	 *
	 */
	private Date updateTime;

	/**
	 *
	 */
	private String smallTitle;

	/**
	 *
	 */
	private Integer applyCount;

	/**
	 *
	 */
	private Date fightEndTime;

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
	public String getGameName() {
		return gameName;
	}

	/**
	 *
	 */
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	/**
	 *
	 */
	public String getGameType() {
		return gameType;
	}

	/**
	 *
	 */
	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	/**
	 *
	 */
	public String getSignType() {
		return signType;
	}

	/**
	 *
	 */
	public void setSignType(String signType) {
		this.signType = signType;
	}

	/**
	 *
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 *
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 *
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 *
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 *
	 */
	public Date getFightStartTime() {
		return fightStartTime;
	}

	/**
	 *
	 */
	public void setFightStartTime(Date fightStartTime) {
		this.fightStartTime = fightStartTime;
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
	 *
	 */
	public String getSmallTitle() {
		return smallTitle;
	}

	/**
	 *
	 */
	public void setSmallTitle(String smallTitle) {
		this.smallTitle = smallTitle;
	}

	/**
	 *
	 */
	public Integer getApplyCount() {
		return applyCount;
	}

	/**
	 *
	 */
	public void setApplyCount(Integer applyCount) {
		this.applyCount = applyCount;
	}

	/**
	 *
	 */
	public Date getFightEndTime() {
		return fightEndTime;
	}

	/**
	 *
	 */
	public void setFightEndTime(Date fightEndTime) {
		this.fightEndTime = fightEndTime;
	}

}
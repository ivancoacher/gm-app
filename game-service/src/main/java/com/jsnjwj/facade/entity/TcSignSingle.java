package com.jsnjwj.facade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * @TableName tc_sign_single
 */
@TableName(value = "tc_sign_single")
public class TcSignSingle implements Serializable {

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
	private Integer itemId;

	/**
	 *
	 */
	private Integer teamId;

	/**
	 *
	 */
	private String name;

	/**
	 *
	 */
	private String teamName;

	/**
	 *
	 */
	private String sex;

	/**
	 *
	 */
	private Integer age;

	/**
	 *
	 */
	private String remark;

	/**
	 *
	 */
	private Integer signStatus;

	/**
	 *
	 */
	private Integer auditStatus;

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
	public Integer getTeamId() {
		return teamId;
	}

	/**
	 *
	 */
	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}

	/**
	 *
	 */
	public String getName() {
		return name;
	}

	/**
	 *
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 *
	 */
	public String getTeamName() {
		return teamName;
	}

	/**
	 *
	 */
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	/**
	 *
	 */
	public String getSex() {
		return sex;
	}

	/**
	 *
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 *
	 */
	public Integer getAge() {
		return age;
	}

	/**
	 *
	 */
	public void setAge(Integer age) {
		this.age = age;
	}

	/**
	 *
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 *
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 *
	 */
	public Integer getSignStatus() {
		return signStatus;
	}

	/**
	 *
	 */
	public void setSignStatus(Integer signStatus) {
		this.signStatus = signStatus;
	}

	/**
	 *
	 */
	public Integer getAuditStatus() {
		return auditStatus;
	}

	/**
	 *
	 */
	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
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
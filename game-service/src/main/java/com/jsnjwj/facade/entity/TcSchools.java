package com.jsnjwj.facade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * @TableName tc_schools
 */
@TableName(value = "tc_schools")
public class TcSchools implements Serializable {

	/**
	 *
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 *
	 */
	private String scCode;

	/**
	 *
	 */
	private Integer scId;

	/**
	 *
	 */
	private String scName;

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
	private String saName;

	/**
	 *
	 */
	private String saEmail;

	/**
	 *
	 */
	private String saTel;

	/**
	 *
	 */
	private String saStatus;

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
	public String getScCode() {
		return scCode;
	}

	/**
	 *
	 */
	public void setScCode(String scCode) {
		this.scCode = scCode;
	}

	/**
	 *
	 */
	public Integer getScId() {
		return scId;
	}

	/**
	 *
	 */
	public void setScId(Integer scId) {
		this.scId = scId;
	}

	/**
	 *
	 */
	public String getScName() {
		return scName;
	}

	/**
	 *
	 */
	public void setScName(String scName) {
		this.scName = scName;
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
	public String getSaName() {
		return saName;
	}

	/**
	 *
	 */
	public void setSaName(String saName) {
		this.saName = saName;
	}

	/**
	 *
	 */
	public String getSaEmail() {
		return saEmail;
	}

	/**
	 *
	 */
	public void setSaEmail(String saEmail) {
		this.saEmail = saEmail;
	}

	/**
	 *
	 */
	public String getSaTel() {
		return saTel;
	}

	/**
	 *
	 */
	public void setSaTel(String saTel) {
		this.saTel = saTel;
	}

	/**
	 *
	 */
	public String getSaStatus() {
		return saStatus;
	}

	/**
	 *
	 */
	public void setSaStatus(String saStatus) {
		this.saStatus = saStatus;
	}

}
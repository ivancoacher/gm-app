package com.jsnjwj.facade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * @TableName tc_judge
 */
@TableName(value = "tc_judge")
public class TcJudge implements Serializable {

	/**
	 *
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 *
	 */
	private String judgeName;

	/**
	 *
	 */
	private String phone;

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
	public String getJudgeName() {
		return judgeName;
	}

	/**
	 *
	 */
	public void setJudgeName(String judgeName) {
		this.judgeName = judgeName;
	}

	/**
	 *
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 *
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

}
package com.jsnjwj.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * (CContractFile)实体类
 *
 * @author makejava
 * @since 2023-06-23 02:06:19
 */
@Data
@TableName("admin_user")
public class AdminUser {

	@TableId(type = IdType.AUTO)
	private Long id;

	private String username;

	private String avatar;

	private String passwordHash;

	/**
	 * 1：正常；0：禁用
	 */
	private Integer enable;

	private String phone;

	private String realname;

	private String remark;

	/**
	 * 所属组织编号
	 */
	private Long deptId;

	private Integer sex;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createAt;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateAt;

}

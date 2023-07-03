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
@TableName("c_users")
public class User {

	@TableId(type = IdType.AUTO)
	private Long id;

	private String username;

	private String avatar;

	private String password;

	private String role;

	private String info;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

}

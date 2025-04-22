package com.jsnjwj.facade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName vote_school
 */
@Data
@TableName(value = "vote_school")
public class VoteSchoolEntity implements Serializable {

	/**
	 *
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 学校名
	 */
	private String school;

	/**
	 * 区域
	 */
	private Integer area;

	/**
	 * 票数
	 */
	private Integer voteNum;

}
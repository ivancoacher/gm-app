package com.jsnjwj.facade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName vote_detail
 */
@Data
@TableName(value = "vote_detail")
public class VoteDetailEntity implements Serializable {

	/**
	 *
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	private Long schoolId;

	private Integer type;

}
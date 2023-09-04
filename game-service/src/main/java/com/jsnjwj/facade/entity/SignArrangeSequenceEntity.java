package com.jsnjwj.facade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @TableName tc_game_area
 */
@Data
@TableName(value = "tc_sign_arrange_sequence")
public class SignArrangeSequenceEntity implements Serializable {

	/**
	 *
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	private Long gameId;

	/**
	 * 报名编号
	 * 单人或队伍
	 */
	private Long signId;

	private Integer sort;

	/**
	 * 分组编排编号
	 */
	private Integer recordId;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

}
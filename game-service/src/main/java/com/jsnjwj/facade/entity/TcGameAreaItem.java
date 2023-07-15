package com.jsnjwj.facade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName tc_game_area_item
 */
@Data
@TableName(value = "tc_game_area_item")
public class TcGameAreaItem implements Serializable {

	/**
	 *
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 *
	 */
	private String areaId;

	/**
	 *
	 */
	private Integer gameId;

	/**
	 *
	 */
	private Integer itemId;

}
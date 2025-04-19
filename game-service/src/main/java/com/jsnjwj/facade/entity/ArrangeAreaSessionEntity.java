package com.jsnjwj.facade.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName tc_game_area
 */
@Data
@TableName(value = "tc_game_area_session")
public class ArrangeAreaSessionEntity extends BaseEntity {

	/**
	 * 赛事id
	 */
	private Long gameId;

	private Long areaId;

	private Integer areaNo;

	private Long sessionId;

	private Integer sort = 0;

}
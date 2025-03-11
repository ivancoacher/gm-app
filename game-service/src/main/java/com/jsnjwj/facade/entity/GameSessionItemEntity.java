package com.jsnjwj.facade.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName tc_game_area
 */
@Data
@TableName(value = "tc_game_session_item")
public class GameSessionItemEntity extends BaseEntity {

    /**
     * 赛事id
     */
    private Long gameId;

    /**
     * 场次名称
     */
    private Long sessionId;

    /**
     * 状态
     */
    private Integer sort;

    /**
     * 项目id
     */
    private Long itemId;

}
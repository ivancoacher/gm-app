package com.jsnjwj.facade.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName tc_game_area
 */
@Data
@TableName(value = "tc_game_session_setting")
public class GameSessionSettingEntity extends BaseEntity {

    /**
     * 赛事id
     */
    private Long gameId;

    /**
     * 场次名称
     */
    private Long sessionId;

    private Integer orgMin = 0;
    private Integer orgMax = 5;
    private Integer teamMin = 0;
    private Integer teamMax = 5;
    private Integer singleMin = 0;
    private Integer singleMax = 5;

}
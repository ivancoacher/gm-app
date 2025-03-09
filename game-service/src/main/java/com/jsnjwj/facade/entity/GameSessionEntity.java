package com.jsnjwj.facade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName tc_game_area
 */
@Data
@TableName(value = "tc_game_session")
public class GameSessionEntity implements Serializable {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 赛事id
     */
    private Long gameId;

    /**
     * 场次名称
     */
    private String sessionName;

    /**
     * 状态
     */
    private Integer status;

    /**
     *
     */
    private Integer sessionNo;

}
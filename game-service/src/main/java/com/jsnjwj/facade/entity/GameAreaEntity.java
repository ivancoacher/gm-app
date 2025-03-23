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
@TableName(value = "tc_game_area")
public class GameAreaEntity implements Serializable {

    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     *
     */
    private Long gameId;

    /**
     *
     */
    private String areaName;

    /**
     *
     */
    private Integer status;

    /**
     *
     */
    private Integer areaNo;

}
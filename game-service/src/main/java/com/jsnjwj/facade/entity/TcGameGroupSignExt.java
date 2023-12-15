package com.jsnjwj.facade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName tc_game_group_sign_ext
 */
@Data
@TableName(value = "tc_game_group_sign_ext")
public class TcGameGroupSignExt implements Serializable {

    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     *
     */
    private Integer gameId;

    /**
     *
     */
    private String dictCode;

    /**
     *
     */
    private String singValue;

    /**
     *
     */
    private Integer singForce;

}
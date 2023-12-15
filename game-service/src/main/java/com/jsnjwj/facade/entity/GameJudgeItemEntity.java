package com.jsnjwj.facade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName tc_game_judge_item
 */
@Data
@TableName(value = "tc_game_judge_item")
public class GameJudgeItemEntity implements Serializable {

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
    private Integer judgeId;

    /**
     *
     */
    private Integer itemId;

}
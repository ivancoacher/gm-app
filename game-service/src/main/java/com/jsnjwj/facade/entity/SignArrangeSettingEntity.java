package com.jsnjwj.facade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jsnjwj.facade.enums.SettingRuleEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "tc_sign_arrange_setting")
public class SignArrangeSettingEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("game_id")
    private Long gameId;

    @TableField("group_id")
    private Long groupId;

    @TableField("item_id")
    private Long itemId;

    /**
     *
     */
    @TableField("judge_group_num")
    private Integer judgeGroupNum;

    /**
     * 积分规则
     */
    @TableField("score_rule")
    private SettingRuleEnum scoreRule;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}

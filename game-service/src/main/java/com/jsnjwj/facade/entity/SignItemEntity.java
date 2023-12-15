package com.jsnjwj.facade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName tc_sign_item
 */
@Data
@TableName(value = "tc_sign_item")
public class SignItemEntity implements Serializable {

    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

}
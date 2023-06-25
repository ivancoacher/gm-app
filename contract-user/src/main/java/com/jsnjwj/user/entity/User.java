package com.jsnjwj.user.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * (CContractFile)实体类
 *
 * @author makejava
 * @since 2023-06-23 02:06:19
 */
@Data
@TableName("c_users")
public class User implements Serializable {
    private static final long serialVersionUID = -54218611872513195L;
    @TableId
    private Integer id;

    private String username;

    private String password;
}


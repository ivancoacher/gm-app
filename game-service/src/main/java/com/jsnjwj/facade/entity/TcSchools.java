package com.jsnjwj.facade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName tc_schools
 */
@Data
@TableName(value = "tc_schools")
public class TcSchools implements Serializable {

    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     *
     */
    private String scCode;

    /**
     *
     */
    private Integer scId;

    /**
     *
     */
    private String scName;

    /**
     *
     */
    private Date createTime;

    /**
     *
     */
    private Date updateTime;

    /**
     *
     */
    private String saName;

    /**
     *
     */
    private String saEmail;

    /**
     *
     */
    private String saTel;

    /**
     *
     */
    private String saStatus;

}
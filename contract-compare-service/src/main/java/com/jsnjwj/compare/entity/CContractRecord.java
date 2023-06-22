package com.jsnjwj.compare.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * (CContractRecord)实体类
 *
 * @author makejava
 * @since 2023-06-23 02:06:22
 */
public class CContractRecord implements Serializable {
    private static final long serialVersionUID = 748741055830395712L;

    private Integer id;

    private Integer userId;

    private Integer status;
    /**
     * 原文档编号
     */
    private Long originFileId;
    /**
     * 对比文档编号
     */
    private Long compareFileId;

    private Date createTime;

    private Date updateTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getOriginFileId() {
        return originFileId;
    }

    public void setOriginFileId(Long originFileId) {
        this.originFileId = originFileId;
    }

    public Long getCompareFileId() {
        return compareFileId;
    }

    public void setCompareFileId(Long compareFileId) {
        this.compareFileId = compareFileId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}


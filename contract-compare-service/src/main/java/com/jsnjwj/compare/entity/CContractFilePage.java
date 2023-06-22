package com.jsnjwj.compare.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * (CContractFilePage)实体类
 *
 * @author makejava
 * @since 2023-06-23 02:06:22
 */
public class CContractFilePage implements Serializable {
    private static final long serialVersionUID = -31768408083590278L;

    private Integer id;
    /**
     * 文件编号
     */
    private Integer fileId;
    /**
     * 页面编号
     */
    private Integer pageNo;
    /**
     * 页面地址
     */
    private String pagePath;

    private Date createTime;

    private Date updateTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public String getPagePath() {
        return pagePath;
    }

    public void setPagePath(String pagePath) {
        this.pagePath = pagePath;
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


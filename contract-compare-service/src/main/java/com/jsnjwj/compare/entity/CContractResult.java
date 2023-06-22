package com.jsnjwj.compare.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * (CContractResult)实体类
 *
 * @author makejava
 * @since 2023-06-23 02:06:23
 */
public class CContractResult implements Serializable {
    private static final long serialVersionUID = -20285122907714444L;

    private Integer id;
    /**
     * 合同对比业务编号
     */
    private Long contractId;
    /**
     * 页面编号
     */
    private Long pageId;
    /**
     * 识别内容
     */
    private Object content;

    private Date createTime;

    private Date updateTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Long getPageId() {
        return pageId;
    }

    public void setPageId(Long pageId) {
        this.pageId = pageId;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
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


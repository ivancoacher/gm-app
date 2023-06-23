package com.jsnjwj.compare.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * (CContractResult)实体类
 *
 * @author makejava
 * @since 2023-06-23 02:06:23
 */
@Data
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
    private String content;

    private Date createTime;

    private Date updateTime;

}


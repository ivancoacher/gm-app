package com.jsnjwj.facade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("c_contract_result")

public class CContractResultEntity implements Serializable {

	private static final long serialVersionUID = -20285122907714444L;

	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 合同对比业务编号
	 */
	private Long contractId;

	/**
	 * 页面编号
	 */
	private Long pageId;

	private Integer pageNo;

	/**
	 * 识别内容
	 */
	private String content;

	private Date createTime;

	private Date updateTime;

}

package com.jsnjwj.facade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * (CContractFilePage)实体类
 *
 * @author makejava
 * @since 2023-06-23 02:06:22
 */
@Data
@TableName("c_contract_file_page")

public class CContractFilePageEntity implements Serializable {

	private static final long serialVersionUID = -31768408083590278L;

	@TableId(type = IdType.AUTO)
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

	private String compareResult;

	private Integer contractId;

}

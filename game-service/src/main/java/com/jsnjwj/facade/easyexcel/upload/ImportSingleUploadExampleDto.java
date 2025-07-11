package com.jsnjwj.facade.easyexcel.upload;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.util.StringUtils;
import lombok.Data;

import java.util.Objects;

@Data
public class ImportSingleUploadExampleDto {

	@ExcelProperty(value = "项目类别")
	private String itemType;

	@ExcelProperty(value = "比赛项目")
	private String itemName;

	@ExcelProperty(value = "组别")
	private String groupName;

	@ExcelProperty(value = "姓名")
	private String name;

	/**
	 * 年龄
	 */
	@ExcelProperty(value = "年龄")
	private String age;

	/**
	 * 性别
	 */
	@ExcelProperty(value = "性别")
	private String sex;

	@ExcelProperty(value = "号码牌")
	private String gameNum;

	@ExcelProperty(value = "学籍号")
	private String studentNum;

	@ExcelProperty(value = "证件号")
	private String cardNum;

	@ExcelProperty(value = "国籍")
	private String country;

	@ExcelProperty(value = "民族")
	private String nation;

	@ExcelProperty(value = "学校")
	private String orgName;

	@ExcelProperty(value = "团队")
	private String teamName;

	@ExcelProperty(value = "领队")
	private String leaderName;

	@ExcelProperty(value = "领队联系电话")
	private String leaderPhone;

	@ExcelProperty(value = "教练")
	private String coachName;

	@ExcelProperty(value = "教练联系电话")
	private String coachPhone;

	@ExcelProperty(value = "备注")
	private String remark;

}

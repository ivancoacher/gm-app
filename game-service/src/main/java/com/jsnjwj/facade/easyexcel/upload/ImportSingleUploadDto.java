package com.jsnjwj.facade.easyexcel.upload;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.util.StringUtils;
import lombok.Data;

import java.util.Objects;

@Data
public class ImportSingleUploadDto {

	@ExcelProperty(value = "比赛类别")
	private String gameType;

	@ExcelProperty(value = "比赛项目")
	private String itemName;

	@ExcelProperty(value = "组别")
	private String groupName;

	@ExcelProperty(value = "姓名")
	private String name;

	/**
	 *
	 */
	@ExcelProperty(value = "年龄")
	private String age;

	/**
	 *
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

	/**
	 * 组别编号
	 */
	private String groupId;

	/**
	 * 项目编号
	 */
	private String itemId;

	/**
	 * 队伍编号
	 */
	private String teamId;

	public Long getGroupId() {
		return StringUtils.isNotBlank(groupId) ? Long.parseLong(groupId) : 0L;
	}

	// public void setGroupId(Long groupId){
	// if (!Objects.isNull(groupId)){
	// this.groupId = String.valueOf(groupId);
	// }
	// }

	public Long getItemId() {
		return StringUtils.isNotBlank(itemId) ? Long.parseLong(itemId) : 0L;
	}

	// public void setItemId(Long itemId){
	// if (!Objects.isNull(groupId)){
	// this.itemId = String.valueOf(itemId);
	// }
	// }
	public Long getTeamId() {
		return StringUtils.isNotBlank(teamId) ? Long.parseLong(teamId) : 0L;
	}

	// public void setTeamId(Long teamId){
	// if (!Objects.isNull(teamId)){
	// this.teamId = String.valueOf(teamId);
	// }
	// }

}

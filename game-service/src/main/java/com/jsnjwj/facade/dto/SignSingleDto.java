package com.jsnjwj.facade.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SignSingleDto {

	private Long applyId;

	private Long teamId;

	private Long gameId;

	private String teamName;

	private String name;

	private Integer sex;

	private Integer age;

	private Long groupId;

	private String groupName;

	private Long itemId;

	private String itemName;

	private String remark;

	private Date createTime;

	private Date updateTime;

	private Integer signStatus;

	private String signStatusDesc;

	private SignTeamDto team;

}

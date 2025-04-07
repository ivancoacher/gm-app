package com.jsnjwj.facade.dao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionDrawDao {

	private Long drawId;

	private Long gameId;

	private Long sessionId;

	private Integer sessionNo;

	private String sessionName;

	private Long signId;

	private String signName;

	private String signOrg;

	private Long groupId;

	private Long itemId;

	private String itemName;

	private String groupName;

	private Long teamId;

	private String teamName;

	private Integer sort;

	/**
	 * 1：集体项目
	 * 2：个人项目
	 */
	private Integer drawType;

}

package com.jsnjwj.facade.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArrangeDrawDto {

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

}

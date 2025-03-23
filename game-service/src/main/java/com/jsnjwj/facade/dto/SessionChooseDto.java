package com.jsnjwj.facade.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionChooseDto {

	private Long sessionId;

	private Long areaId;

	private boolean disabled;

	private String sessionName;

}

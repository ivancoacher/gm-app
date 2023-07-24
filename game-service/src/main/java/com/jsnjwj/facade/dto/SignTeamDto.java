package com.jsnjwj.facade.dto;

import lombok.Data;

@Data
public class SignTeamDto {

	private Long gameId;

	private Long teamId;

	private String teamName;

	private String leaderName;

	private String leaderPhone;

	private String remark;

}

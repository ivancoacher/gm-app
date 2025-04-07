package com.jsnjwj.facade.dto.draw;

import com.jsnjwj.facade.dto.SignSingleDto;
import com.jsnjwj.facade.dto.SignTeamDto;
import com.jsnjwj.facade.vo.session.SessionItemVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DrawResultUnit {

	private Long sessionId;

	private Long itemId;

	private Long teamId;

	private Long signId;

	private SessionItemVo project;

	private SignSingleDto player;

	private SignTeamDto team;

	private Integer order;

}

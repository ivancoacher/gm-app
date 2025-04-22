package com.jsnjwj.facade.vo.session;

import com.jsnjwj.facade.dto.SignSingleDto;
import com.jsnjwj.facade.dto.SignTeamDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SessionItemVo {

	private String groupName;

	private String itemName;

	private Long itemId;

	private Long sessionId;

	private Integer sort;

	private Integer itemType;

	private List<SignSingleDto> singleList = new ArrayList<>();

	private List<SignTeamDto> teamList = new ArrayList<>();

}

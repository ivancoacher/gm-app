package com.jsnjwj.facade.dto;

import com.jsnjwj.facade.vo.session.SessionItemVo;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ArrangeSessionVo {

	private Long sessionId;

	private String sessionName;

	private Integer sessionNo;

	/**
	 * 已排项目列表
	 */
	private List<SessionItemVo> itemList = new ArrayList<>();

	/**
	 * 已排项目数量
	 */
	private Integer itemNum = 0;

}

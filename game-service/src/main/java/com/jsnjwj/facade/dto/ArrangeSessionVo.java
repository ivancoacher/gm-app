package com.jsnjwj.facade.dto;

import com.jsnjwj.facade.vo.session.SessionItemVo;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Arrays;
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

	/**
	 * 抽签规则
	 */
	private SessionSettingVo sessionSetting = new SessionSettingVo();

	@Getter
	@Setter
	public static class SessionSettingVo {

		private List<Integer> orgRange = Arrays.asList(0, 0);

		private List<Integer> teamRange = Arrays.asList(0, 0);

		private List<Integer> singleRange = Arrays.asList(0, 0);

	}

}

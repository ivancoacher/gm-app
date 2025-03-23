package com.jsnjwj.facade.query.session;

import com.jsnjwj.common.request.BaseRequest;
import com.jsnjwj.facade.dto.ArrangeSessionVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class GameGroupingSessionSetQuery extends BaseRequest {

	private String sessionName;

	private Integer sessionNo;

	private Integer status;

	private Long sessionId;

	private SessionSettingVo sessionSetting = new SessionSettingVo();

	@Getter
	@Setter
	public static class SessionSettingVo {

		private List<Integer> orgRange = Arrays.asList(3, 5);

		private List<Integer> teamRange = Arrays.asList(3, 5);

		private List<Integer> singleRange = Arrays.asList(3, 5);

	}

}

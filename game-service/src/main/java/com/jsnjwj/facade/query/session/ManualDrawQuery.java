package com.jsnjwj.facade.query.session;

import com.jsnjwj.common.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ManualDrawQuery extends BaseRequest {

	private List<ManualDrawData> data;

	private Integer sessionNo;

	private Long sessionId;


	@Getter
	@Setter
	public static class ManualDrawData {

		/**
		 * 兼容不按场次编排的情况
		 */
		private Long drawId;

		private Integer sort;

	}

}

package com.jsnjwj.facade.query;

import com.jsnjwj.common.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ManualDrawAreaSessionQuery extends BaseRequest {

	private List<ManualDrawData> data;

	private Long areaId;

	@Getter
	@Setter
	public static class ManualDrawData {

		/**
		 * 兼容不按场次编排的情况
		 */
		private Long sessionId;

		private Integer sort;

	}

}

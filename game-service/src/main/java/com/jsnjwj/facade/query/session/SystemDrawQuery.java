package com.jsnjwj.facade.query.session;

import com.jsnjwj.common.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SystemDrawQuery extends BaseRequest {

	/**
	 * @see com.jsnjwj.facade.enums.DrawTypeEnum
	 */
	private Integer type;

	private Integer sessionNo;

	private Long sessionId;

}

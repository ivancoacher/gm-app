package com.jsnjwj.facade.query;

import com.jsnjwj.common.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GameGroupingViewQuery extends BaseRequest {

	private Long groupId;

	private Integer page = 1;

	private Integer pageSize = 10;

}

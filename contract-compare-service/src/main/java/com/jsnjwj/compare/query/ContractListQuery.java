package com.jsnjwj.compare.query;

import com.jsnjwj.common.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ContractListQuery extends BaseRequest {

	private Integer pageIndex = 1;

	private Integer pageSize = 10;

	private Integer userId;

}

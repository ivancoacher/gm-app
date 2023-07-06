package com.jsnjwj.compare.query;

import com.jsnjwj.common.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ContractDetailQuery extends BaseRequest {

	private String contractId;

}

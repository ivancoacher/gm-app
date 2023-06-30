package com.jsnjwj.compare.query;

import com.jsnjwj.common.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ComparePagesQuery extends BaseRequest {

	private Integer contractId;

	private Integer pageNo;

	private Integer fileId;

}

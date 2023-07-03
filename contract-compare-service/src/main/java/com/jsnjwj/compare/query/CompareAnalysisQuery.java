package com.jsnjwj.compare.query;

import com.jsnjwj.common.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CompareAnalysisQuery extends BaseRequest {

	private String startTime;

	private String endTime;

}

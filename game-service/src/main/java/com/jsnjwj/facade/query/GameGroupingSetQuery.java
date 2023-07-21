package com.jsnjwj.facade.query;

import com.jsnjwj.common.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GameGroupingSetQuery extends BaseRequest {

	private String areaName;

	private Integer areaNo;

	private Integer status;

	private Integer areaId;

}

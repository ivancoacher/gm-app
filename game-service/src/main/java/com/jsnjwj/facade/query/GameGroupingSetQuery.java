package com.jsnjwj.facade.query;

import com.jsnjwj.common.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class GameGroupingSetQuery extends BaseRequest {

	private Integer areaNo;

	private List<Long> itemIds;

	private Integer areaId;

}

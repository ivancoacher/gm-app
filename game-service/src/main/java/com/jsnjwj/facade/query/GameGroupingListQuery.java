package com.jsnjwj.facade.query;

import com.jsnjwj.common.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class GameGroupingListQuery extends BaseRequest {

	private Integer areaId;

}

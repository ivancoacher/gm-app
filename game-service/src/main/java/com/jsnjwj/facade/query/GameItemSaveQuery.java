package com.jsnjwj.facade.query;

import com.jsnjwj.common.request.BaseRequest;
import lombok.Data;

@Data
public class GameItemSaveQuery extends BaseRequest {

	private String itemName;

	private Long groupId;

	private Integer sort;

}

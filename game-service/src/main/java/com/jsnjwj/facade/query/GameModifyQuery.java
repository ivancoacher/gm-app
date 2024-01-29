package com.jsnjwj.facade.query;

import com.jsnjwj.common.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GameModifyQuery extends BaseRequest {

	private String gameName;

	private Integer gameType;

	private String gameStartTime;

	private String gameEndTime;

	private String signStartTime;

	private String signEndTime;

	private Integer signType;

	private String gameLocation;

	private Integer status;

}

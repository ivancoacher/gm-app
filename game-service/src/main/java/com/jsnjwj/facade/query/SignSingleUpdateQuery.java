package com.jsnjwj.facade.query;

import com.jsnjwj.common.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SignSingleUpdateQuery extends BaseRequest {

	private Long applyId;

	private String name;

	private int age;

	private int sex;

	private String orgName;

}

package com.jsnjwj.facade.query.rule;

import com.jsnjwj.common.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ItemRuleSetQuery extends BaseRequest {

	private Long itemId;

	private Integer ruleId;

	private Boolean syncGroup = false;

	private Long groupId;

}

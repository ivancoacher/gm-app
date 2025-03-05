package com.jsnjwj.facade.query.rule;

import com.jsnjwj.common.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ItemRuleSetQuery extends BaseRequest {

    private Long gameId;

    private Long itemId;

    private Integer ruleId;

    private Long groupId;

}

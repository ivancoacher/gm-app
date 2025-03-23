package com.jsnjwj.facade.query.rule;

import com.jsnjwj.common.request.BasePageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ItemRuleQuery extends BasePageRequest {

    private Long gameId;

    private Long itemId;

    private Long ruleId;

    private Long groupId;

}

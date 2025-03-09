package com.jsnjwj.facade.query.rule;

import com.jsnjwj.common.request.BasePageRequest;
import com.jsnjwj.common.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ItemRuleQuery extends BasePageRequest {

    private Long gameId;

    private Long itemId;

    private Long ruleId;

    private Long groupId;

}

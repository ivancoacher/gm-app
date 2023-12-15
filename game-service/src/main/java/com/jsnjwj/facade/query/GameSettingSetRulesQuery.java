package com.jsnjwj.facade.query;

import com.jsnjwj.common.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class GameSettingSetRulesQuery extends BaseRequest {

    private Long gameId;

    private Long itemId;

    private Integer judgeGroupNum;

    private Integer scoreRule;

    private List<RuleContent> ruleContentList;

    @Data
    public static class RuleContent {

        public Integer ruleRatio;

        public Integer ruleWeight;

        public Integer ruleExtraType;

        public String ruleExtraName;

    }

}

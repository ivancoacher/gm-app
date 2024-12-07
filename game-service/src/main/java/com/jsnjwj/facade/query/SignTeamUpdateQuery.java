package com.jsnjwj.facade.query;

import com.jsnjwj.common.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SignTeamUpdateQuery extends BaseRequest {

    private Long teamId;

    private String teamName;

    private Long orgId;

    private String orgName;

}

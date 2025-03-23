package com.jsnjwj.facade.query;

import com.jsnjwj.common.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SignTeamListQuery extends BaseRequest {

    private Long groupId;

    private String title;

    private Integer page = 1;

    private Integer limit = 10;

    private Long teamId;

    private Long orgId;

}

package com.jsnjwj.facade.query;

import com.jsnjwj.common.request.BaseRequest;
import lombok.Data;

@Data
public class SignSingleListQuery extends BaseRequest {

    private Long groupId;

    private Long itemId;

    private Long teamId;

    private Long orgId;

    private String key;

    private Integer page = 1;

    private Integer pageSize = 10;

}

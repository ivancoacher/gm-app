package com.jsnjwj.trade.request;

import com.jsnjwj.common.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class QueryListRequest extends BaseRequest {

    private String startTime;

    private String endTime;

    private String key;

    private Integer pageIndex = 1;

    private Integer pageSize = 10;

}

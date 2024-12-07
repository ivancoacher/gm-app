package com.jsnjwj.user.request;

import com.jsnjwj.common.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FetchOptLogRequest extends BaseRequest {

    private Integer pageIndex = 1;

    private Integer pageSize = 10;

    private String startTime;

    private String endTime;

}

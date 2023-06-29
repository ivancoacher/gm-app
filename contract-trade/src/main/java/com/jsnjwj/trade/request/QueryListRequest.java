package com.jsnjwj.trade.request;

import com.jsnjwj.common.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class QueryListRequest extends BaseRequest {
    private LocalTime startTime;
    private LocalTime endTime;
}

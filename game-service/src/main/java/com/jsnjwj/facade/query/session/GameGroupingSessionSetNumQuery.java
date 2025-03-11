package com.jsnjwj.facade.query.session;

import com.jsnjwj.common.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GameGroupingSessionSetNumQuery extends BaseRequest {

    private Integer sessionNum;

}

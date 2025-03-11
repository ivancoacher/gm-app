package com.jsnjwj.facade.query.session;

import com.jsnjwj.common.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GameGroupingSessionSetQuery extends BaseRequest {

    private String sessionName;

    private Integer sessionNo;

    private Integer status;

    private Long sessionId;

}

package com.jsnjwj.facade.query;

import com.jsnjwj.common.request.BaseRequest;
import lombok.Data;

@Data
public class GameGroupUpdateQuery extends BaseRequest {

    private String groupName;

    private Integer sort;

    private Long groupId;

}

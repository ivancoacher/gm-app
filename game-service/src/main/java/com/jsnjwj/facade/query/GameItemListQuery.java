package com.jsnjwj.facade.query;

import com.jsnjwj.common.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GameItemListQuery extends BaseRequest {

    private Long groupId;

    private String itemName;

    private Integer page;


    private Integer limit;
}

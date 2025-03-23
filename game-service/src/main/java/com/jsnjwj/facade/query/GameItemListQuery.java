package com.jsnjwj.facade.query;

import com.jsnjwj.common.request.BasePageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GameItemListQuery extends BasePageRequest {

    private Long groupId;

    private String itemName;

}

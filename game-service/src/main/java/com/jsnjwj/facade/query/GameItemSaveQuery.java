package com.jsnjwj.facade.query;

import com.jsnjwj.common.request.BaseRequest;
import lombok.Data;

import java.util.List;

@Data
public class GameItemSaveQuery extends BaseRequest {

    private String itemName;

    private List<Long> groupId;

    private String itemType;

    private Integer sort;

}

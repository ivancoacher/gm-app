package com.jsnjwj.facade.dto;

import lombok.Data;

@Data
public class GroupingItemDto {

    private Long groupId;

    private Long gameId;

    private Long itemId;

    private String groupName;

    private String itemName;

    private String rule;

    private Integer judgeCount;

    private String scoreRule;

}

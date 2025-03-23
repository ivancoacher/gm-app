package com.jsnjwj.facade.dto;

import lombok.Data;

@Data
public class GameItemDto {

    public Long gameId;

    public Long itemId;

    public Long groupId;

    public String itemName;

    public String groupName;

    public Integer sort;

}

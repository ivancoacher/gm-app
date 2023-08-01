package com.jsnjwj.facade.dto;

import lombok.Data;

@Data
public class GameAreaDto {
    public Long areaId;
    
    public Long gameId;

    public Long itemId;

    public Long groupId;

    public Integer areaNo;

    public String areaName;
}

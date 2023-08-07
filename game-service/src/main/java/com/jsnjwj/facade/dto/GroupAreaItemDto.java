package com.jsnjwj.facade.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class GroupAreaItemDto {

	public GameAreaDto gameArea;

	public List<GameItemDto> itemList;

}

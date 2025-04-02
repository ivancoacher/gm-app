package com.jsnjwj.facade.query;

import lombok.Data;

@Data
public class GameItemUpdateQuery {

	private Long id;

	private Long itemId;

	private String itemName;

	private Long groupId;

	private Integer sort;

	private Integer itemType;

}

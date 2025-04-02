package com.jsnjwj.facade.vo;

import com.jsnjwj.facade.enums.ItemTypeEnum;
import lombok.Data;

@Data
public class ItemLabelVo {

	private Long itemId;

	private String itemName;

	private Long groupId;

	private String groupName;

	private Long gameId;

	private Integer sort;

	private Integer itemType;

	private String itemTypeName;

	public String getItemTypeName() {

		return ItemTypeEnum.getNameByType(itemType);
	}
}

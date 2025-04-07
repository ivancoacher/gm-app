package com.jsnjwj.facade.enums;

import lombok.Getter;

@Getter
public enum ItemTypeEnum {

	TYPE_TEAM(1, "集体项目"), TYPE_SINGLE(2, "个人项目");

	private final String name;

	private final Integer type;

	ItemTypeEnum(Integer type, String name) {
		this.name = name;
		this.type = type;
	}

	public static Integer getTypeByName(String name) {
		for (ItemTypeEnum itemTypeEnum : ItemTypeEnum.values()) {
			if (itemTypeEnum.getName().equals(name)) {
				return itemTypeEnum.getType();
			}
		}
		return null;
	}

	public static String getNameByType(Integer type) {
		for (ItemTypeEnum itemTypeEnum : ItemTypeEnum.values()) {
			if (itemTypeEnum.getType().equals(type)) {
				return itemTypeEnum.getName();
			}
		}
		return null;
	}

}

package com.jsnjwj.facade.enums;

import lombok.Getter;

@Getter
public enum DrawTypeEnum {

	DRAW_WITH_SESSION(1, "按场次编排"), DRAW_WITHOUT_SESSION(2, "不按场次编排"),;

	private final int type;

	private final String desc;

	DrawTypeEnum(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

}

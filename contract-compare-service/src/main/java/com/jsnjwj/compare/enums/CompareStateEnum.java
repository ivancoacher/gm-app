package com.jsnjwj.compare.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum CompareStateEnum {

	HANDLING(0, "处理中"), HANDLE_SUCCESS(1, "处理成功"), HANDLE_RETRY(2, "处理重试"), HANDLE_FAIL(3, "处理失败");

	private final Integer code;

	private final String value;

	public static String getValue(Integer code) {
		for (CompareStateEnum value : CompareStateEnum.values()) {
			if (Objects.equals(value.getCode(), code)) {
				return value.getValue();
			}
		}
		return null;
	}

}

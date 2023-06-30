package com.jsnjwj.compare.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CompareStateEnum {

	HANDLING(0, "处理中"), HANDLE_SUCCESS(1, "处理成功"), HANDLE_RETRY(2, "处理重试"), HANDLE_FAIL(3, "处理失败");

	@EnumValue
	private final int compareState;

	@JsonValue
	private final String descp;

}

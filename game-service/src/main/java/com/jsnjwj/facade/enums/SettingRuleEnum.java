package com.jsnjwj.facade.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum SettingRuleEnum {

	SETTING_RULE1(1, "去掉最高最低分取平均"),
	SETTING_RULE2(2, "去掉最高最低分求和"),
	SETTING_RULE3(3, "得分取平均"),
	SETTING_RULE4(4, "得分求和"),
	SETTING_RULE5(5, "去掉2个最高分最低分求和"),
	SETTING_RULE6(6, "得分取平均（裁判一号分数除2减二号分数）");

	@EnumValue
	private final Integer code;

	private final String value;

	public static String getValue(Integer code) {
		for (SettingRuleEnum value : SettingRuleEnum.values()) {
			if (Objects.equals(value.getCode(), code)) {
				return value.getValue();
			}
		}
		return null;
	}


	public static SettingRuleEnum getByCode(int code){
		for(SettingRuleEnum ruleEnum:SettingRuleEnum.values()){
			if(ruleEnum.getCode()==code){
				return ruleEnum;
			}
		}
		return null;
	}

}

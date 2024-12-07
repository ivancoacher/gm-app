package com.jsnjwj.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OperateTypeEnum {

    DOWNLOAD_REPORT(1001, "下载报告"), VIEW_COMPARE_RESULT(1002, "查看比对结果"), DOC_COMPARE(1003, "提交比对"), REGISTER(1004, "注册"),
    LOGIN(1005, "登录"), RECHARGE(1006, "充值");

    private final int code;

    private final String value;

    OperateTypeEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public static String getValue(Integer code) {
        for (OperateTypeEnum value : OperateTypeEnum.values()) {
            if (value.getCode() == code) {
                return value.getValue();
            }
        }
        return null;
    }

}

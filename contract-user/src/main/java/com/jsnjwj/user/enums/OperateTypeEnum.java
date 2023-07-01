package com.jsnjwj.user.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OperateTypeEnum {

    DOWNLOAD_REPORT(1001, "下载报告"),
    VIEW_COMPARE_RESULT(1002, "查看比对结果"),
    DOC_COMPARE(1003, "提交比对"),
    REGISTER(1004, "注册"),
    LOGIN(1005, "登录"),
    RECHARGE(1006, "充值");
    @EnumValue
    private final int operateType;
    @JsonValue
    private final String desc;


}

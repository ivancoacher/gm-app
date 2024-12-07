package com.jsnjwj.common.enums;

import lombok.Getter;

@Getter
public enum ResponseEnum {

    SUCESS(20000, "请求成功！"), ERROR(9999, "请求失败！"), SIGNATURE_ERROR(8888, "登录超时");

    private final int resultCode;

    private final String resultMsg;

    ResponseEnum(int resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

}

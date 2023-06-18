package com.jsnjwj.www.enums;

import lombok.Getter;

@Getter
public enum ResponseEnum {
    SUCESS(1000,"请求成功！"),
    ERROR(9999,"请求失败！");

    private int resultCode;
    private String resultMsg;

    ResponseEnum(int resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }
}
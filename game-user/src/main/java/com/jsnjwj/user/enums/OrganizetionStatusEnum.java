package com.jsnjwj.user.enums;

import lombok.Getter;

@Getter
public enum OrganizetionStatusEnum {

    NOT_AUTHENTICATED(0, "未认证"), AUTHENTICATING(1, "认证中"), AUTHENTICATED(2, "认证成功"), AUTHENTICATION_FAILED(3, "认证失败");

    private final int code;

    private final String value;

    OrganizetionStatusEnum(int code, String value) {
        this.value = value;
        this.code = code;
    }

}

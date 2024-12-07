package com.jsnjwj.facade.enums;

import lombok.Getter;

@Getter
public enum SignExportTypeEnum {

    TYPE_ALL, TYPE_ITEM_ORG, TYPE_GROUP_ORG, TYPE_TEAM, TYPE_ITEM_TEAM, TYPE_GROUP_TEAM, TYPE_PROJECT,
    TYPE_PROJECT_PLAYER, TYPE_PLAYER_COUNTING, TYPE_PLAYER_TIMES;

    public SignExportTypeEnum getValue(String type) {
        return SignExportTypeEnum.valueOf(type.toUpperCase());
    }

}

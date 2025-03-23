package com.jsnjwj.facade.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum GameStatusEnum {

    // * -1：项目预设；0：待发布；1：报名中；2：报名结束；3：报名公示；4：进行中；5：成绩公示；6：赛事结束；7：赛事延期；8：赛事取消

    GAME_PROJECTED(-1, "项目预设"), GAME_UPCOMING(0, "待发布"), GAME_REGISTRATION_OPEN(1, "报名中"),
    GAME_REGISTRATION_CLOSED(2, "报名结束"), GAME_REGISTRATION_ANNOUNCEMENT(3, "报名公示"), GAME_ONGOING(4, "进行中"),
    GAME_RESULTS_ANNOUNCEMENT(5, "成绩公示"), GAME_EVENT_ENDED(6, "赛事结束"), GAME_EVENT_POSTPONED(7, "赛事延期"),
    GAME_EVENT_CANCELED(8, "赛事取消");

    private final Integer code;

    private final String value;

    public static String getValue(Integer code) {
        for (GameStatusEnum value : GameStatusEnum.values()) {
            if (Objects.equals(value.getCode(), code)) {
                return value.getValue();
            }
        }
        return null;
    }

}

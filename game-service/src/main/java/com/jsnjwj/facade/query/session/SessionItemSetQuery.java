package com.jsnjwj.facade.query.session;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SessionItemSetQuery {

    private Long gameId;

    private Long sessionId;

    private List<SessionItemSetData> data = new ArrayList<>();

    @Getter
    @Setter
    public static class SessionItemSetData {
        private Long itemId;

        private Integer sort;
    }

}

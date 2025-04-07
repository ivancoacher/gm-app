package com.jsnjwj.facade.dto.draw;

import com.jsnjwj.facade.vo.session.SessionItemVo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SessionUnit {

    private Long sessionId;

    private List<SessionItemVo> itemList;

    private Long gameId;
}

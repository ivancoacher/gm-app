package com.jsnjwj.facade.dto;

import com.jsnjwj.facade.vo.session.SessionItemVo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SessionItemDto {

	private Long gameId;

	private String sessionName;

	private List<SessionItemVo> data = new ArrayList<>();

}

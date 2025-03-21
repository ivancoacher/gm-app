package com.jsnjwj.facade.query.session;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SessionItemSetBatchQuery {

	private Long gameId;

	private List<SessionItemSetQuery> data = new ArrayList<>();

}

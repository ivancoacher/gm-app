package com.jsnjwj.facade.service.v2;

import com.jsnjwj.facade.vo.MatchItemSortVo;
import com.jsnjwj.facade.vo.MatchNumberVo;

import java.util.List;

public interface ProjectService {

	boolean createMatchNumber(Long gameId);

	List<MatchNumberVo> getMatchNumbers(Long gameId);

	boolean sortMatch(Long gameId, List<MatchItemSortVo> itemIds);

}

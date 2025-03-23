package com.jsnjwj.facade.service;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.entity.GamesEntity;
import com.jsnjwj.facade.query.GameAddQuery;
import com.jsnjwj.facade.query.GameInfoQuery;
import com.jsnjwj.facade.query.GameListQuery;
import com.jsnjwj.facade.query.GameModifyQuery;
import com.jsnjwj.facade.vo.GameListVo;

public interface GameInfoService {

	ApiResponse<GameListVo> queryList(GameListQuery query);

	ApiResponse<GamesEntity> fetchInfo(GameInfoQuery query);

	ApiResponse<Boolean> update(GameModifyQuery query);

	ApiResponse<Boolean> save(GameAddQuery query);

	ApiResponse<Boolean> changeStatus(GameModifyQuery query);

}

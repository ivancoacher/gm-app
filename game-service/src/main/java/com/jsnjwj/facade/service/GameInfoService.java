package com.jsnjwj.facade.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.entity.TcGames;
import com.jsnjwj.facade.query.*;
import com.jsnjwj.facade.vo.GameInfoVo;
import com.jsnjwj.facade.vo.GameListVo;

public interface GameInfoService {

	ApiResponse<GameListVo> queryList(GameListQuery query);

	ApiResponse<TcGames> fetchInfo(GameInfoQuery query);

	ApiResponse<Boolean> update(GameModifyQuery query);

	ApiResponse<Boolean> save(GameAddQuery query);

	ApiResponse<Boolean> changeStatus(GameModifyQuery query);

}

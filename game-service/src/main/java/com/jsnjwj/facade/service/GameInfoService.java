package com.jsnjwj.facade.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.entity.TcGames;
import com.jsnjwj.facade.query.*;
import com.jsnjwj.facade.vo.GameInfoVo;

public interface GameInfoService {

	ApiResponse<Page<GameInfoVo>> queryList(GameListQuery query);

	ApiResponse<TcGames> fetchInfo(GameInfoQuery query);

	ApiResponse<Boolean> update(GameInfoQuery query);

	ApiResponse<Boolean> save(GameInfoQuery query);

	ApiResponse<Boolean> changeStatus(GameInfoQuery query);

}

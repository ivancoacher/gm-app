package com.jsnjwj.facade.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.query.GameJudgeAssignQuery;
import com.jsnjwj.facade.query.GameJudgeListQuery;
import com.jsnjwj.facade.vo.GameJudgeVo;

import java.util.List;

public interface GameJudgeService {

    ApiResponse<Page<GameJudgeVo>> fetchPage(GameJudgeListQuery query);

    ApiResponse<List<GameJudgeVo>> fetchList(Long gameId);

    ApiResponse<Boolean> assign(GameJudgeAssignQuery query);

}

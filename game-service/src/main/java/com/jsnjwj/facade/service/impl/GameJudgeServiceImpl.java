package com.jsnjwj.facade.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.manager.GameJudgeManager;
import com.jsnjwj.facade.query.GameJudgeAssignQuery;
import com.jsnjwj.facade.query.GameJudgeListQuery;
import com.jsnjwj.facade.service.GameJudgeService;
import com.jsnjwj.facade.vo.GameJudgeVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 合同比对service
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class GameJudgeServiceImpl implements GameJudgeService {

    @Resource
    private GameJudgeManager gameJudgeManager;

    /**
     * 分页结果查询
     *
     * @param query
     * @return
     */
    @Override
    public ApiResponse<Page<GameJudgeVo>> fetchPage(GameJudgeListQuery query) {
        Page<GameJudgeVo> page = new Page<>();
        List<GameJudgeVo> list = gameJudgeManager.fetchGameJudges(query.getGameId());
        page.setRecords(list);

        Long total = gameJudgeManager.fetchGameJudgesCount(query.getGameId());
        page.setTotal(total);

        return ApiResponse.success(page);
    }

    /**
     * 获取全部
     *
     * @param gameId
     * @return
     */
    @Override
    public ApiResponse<List<GameJudgeVo>> fetchList(Long gameId) {
        List<GameJudgeVo> list = gameJudgeManager.fetchList(gameId);

        return ApiResponse.success(list);
    }

    /**
     * 给项目分配裁判
     *
     * @param query
     * @return
     */
    @Override
    public ApiResponse<Boolean> assign(GameJudgeAssignQuery query) {
        return ApiResponse.success(true);
    }

}

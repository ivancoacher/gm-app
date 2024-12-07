package com.jsnjwj.facade.service.v2.impl;

import com.jsnjwj.facade.service.v2.ProjectService;
import com.jsnjwj.facade.vo.MatchItemSortVo;
import com.jsnjwj.facade.vo.MatchNumberVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    /**
     * 创建场次
     */
    @Override
    public boolean createMatchNumber(Long gameId) {

        return true;
    }

    /**
     * 获取场次
     */
    @Override
    public List<MatchNumberVo> getMatchNumbers(Long gameId) {

        return new ArrayList<>();
    }


    /**
     * 场次内-小项排序
     *
     * @param gameId
     * @param itemSortVoList
     * @return
     */
    @Override
    public boolean sortMatch(Long gameId, List<MatchItemSortVo> itemSortVoList) {
        return true;
    }

}

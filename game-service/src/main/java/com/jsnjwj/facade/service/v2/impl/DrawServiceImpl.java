package com.jsnjwj.facade.service.v2.impl;

import com.jsnjwj.facade.service.v2.DrawService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 抽签分组
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DrawServiceImpl implements DrawService {

    /**
     * 抽签分组
     */
    @Override
    public boolean draw(Long gameId, String type) {
        // 查询所有场次

        // 查询各个场次内对应的小项

        // 将各个场次内的小项打乱，排序。相同选手，间隔5位

        return true;
    }

    /**
     * 查询所有排序内容
     */
    public boolean getDraw(Long gameId, String type) {
        // 查询所有场次

        // 查询各个场次内对应的小项

        // 将各个场次内的小项打乱，排序。相同选手，间隔5位

        return true;
    }


    /**
     * 处理单个场次内的选手顺序
     */
    public boolean setDrawSort() {
        return true;
    }
}

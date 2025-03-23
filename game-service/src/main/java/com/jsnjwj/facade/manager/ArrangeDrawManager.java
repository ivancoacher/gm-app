package com.jsnjwj.facade.manager;

import com.jsnjwj.facade.dao.SessionDrawListDao;
import com.jsnjwj.facade.entity.GameDrawEntity;
import com.jsnjwj.facade.mapper.GameDrawMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArrangeDrawManager {

    private final GameDrawMapper drawMapper;

    public void saveBatch(List<GameDrawEntity> list) {
        drawMapper.saveBatch(list);
    }

    /**
     * 获取已抽签分组场次
     *
     * @param gameId
     * @return
     */
    public List<SessionDrawListDao> getSessionList(Long gameId) {
        return drawMapper.getSessionList(gameId);
    }

}

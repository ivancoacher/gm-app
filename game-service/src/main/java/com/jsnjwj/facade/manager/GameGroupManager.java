package com.jsnjwj.facade.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.facade.entity.TcGameGroup;
import com.jsnjwj.facade.mapper.TcGameGroupMapper;
import com.jsnjwj.facade.vo.GroupLabelVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameGroupManager {
    private final TcGameGroupMapper gameGroupMapper;

    public List<GroupLabelVo> fetchGroups(Long gameId) {
        List<GroupLabelVo> groups = new ArrayList<>();
        return groups;
    }

    public Page<GroupLabelVo> fetchGroupPage() {
        Page<GroupLabelVo> page = new Page<GroupLabelVo>();
        return page;
    }

    public int saveGroup(TcGameGroup gameGroup) {
        return gameGroupMapper.insert(gameGroup);
    }

    public int updateGroup(TcGameGroup gameGroup) {
        return gameGroupMapper.insert(gameGroup);
    }

    public int deleteGroup(Long groupId) {
        return gameGroupMapper.deleteById(groupId);
    }
}

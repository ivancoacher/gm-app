package com.jsnjwj.facade.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.facade.query.GameGroupListQuery;
import com.jsnjwj.facade.vo.GameGroupAllVo;
import com.jsnjwj.facade.vo.GameGroupVo;
import com.jsnjwj.facade.vo.GroupLabelVo;
import org.springframework.stereotype.Service;

import java.util.List;


public interface GameGroupService {

    Page<GroupLabelVo> fetchPages(GameGroupListQuery query);

    List<GroupLabelVo> fetchList(GameGroupListQuery query);

    List<GameGroupAllVo> fetchAll(Long gameId);
}

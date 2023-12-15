package com.jsnjwj.facade.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.facade.dto.GroupingDetailDto;
import com.jsnjwj.facade.dto.GroupingItemDto;
import com.jsnjwj.facade.query.GameGroupingViewQuery;

public interface GameGroupingService {

    Page<GroupingItemDto> fetchGroupingItem(GameGroupingViewQuery query);

    GroupingDetailDto fetchGroupingDetail(GameGroupingViewQuery query);

    int updateArrangeRecord();

    int updateArrangeGrouping();

    int initArrangeOrder();
}

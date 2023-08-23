package com.jsnjwj.facade.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.dto.GroupingDetailDto;
import com.jsnjwj.facade.dto.GroupingItemDto;
import com.jsnjwj.facade.entity.TcGameArea;
import com.jsnjwj.facade.query.*;

import java.util.List;

public interface GameGroupingService {

	Page<GroupingItemDto> fetchGroupingItem(GameGroupingViewQuery query);

	GroupingDetailDto fetchGroupingDetail(GameGroupingViewQuery query);

}

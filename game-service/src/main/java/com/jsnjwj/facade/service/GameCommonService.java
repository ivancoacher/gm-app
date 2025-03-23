package com.jsnjwj.facade.service;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.vo.GroupLabelVo;
import com.jsnjwj.facade.vo.ItemLabelVo;

import java.util.List;

public interface GameCommonService {

    ApiResponse<List<GroupLabelVo>> fetchGroup(Long gameId);

    ApiResponse<List<ItemLabelVo>> fetchItemByGroupId(Long gameId, Long groupId);

}

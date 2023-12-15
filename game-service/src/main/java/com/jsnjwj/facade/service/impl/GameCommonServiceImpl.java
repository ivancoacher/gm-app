package com.jsnjwj.facade.service.impl;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.service.GameCommonService;
import com.jsnjwj.facade.vo.GroupLabelVo;
import com.jsnjwj.facade.vo.ItemLabelVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 合同比对service
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class GameCommonServiceImpl implements GameCommonService {

    @Override
    public ApiResponse<List<GroupLabelVo>> fetchGroup(Long gameId) {
        List<GroupLabelVo> response = new ArrayList<>();
        return ApiResponse.success(response);
    }

    @Override
    public ApiResponse<List<ItemLabelVo>> fetchItemByGroupId(Long gameId, Long groupId) {
        List<ItemLabelVo> response = new ArrayList<>();
        return ApiResponse.success(response);
    }

}

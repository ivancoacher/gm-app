package com.jsnjwj.facade.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.dto.SignSingleDto;
import com.jsnjwj.facade.entity.TcSignSingle;
import com.jsnjwj.facade.manager.SignApplyManager;
import com.jsnjwj.facade.query.SignSingleListQuery;
import com.jsnjwj.facade.service.SignApplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 合同比对service
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SignApplyServiceImpl implements SignApplyService {
    private final SignApplyManager signApplyManager;
    @Override
    public ApiResponse<?> fetchSinglePage(SignSingleListQuery query){
        Page<SignSingleDto> page = new Page<>();
        List<SignSingleDto> list = signApplyManager.fetchSignSinglePage(query);
        page.setRecords(list);
        page.setTotal(signApplyManager.fetchSignSingleCount(query));
        return ApiResponse.success(page);
    }
}

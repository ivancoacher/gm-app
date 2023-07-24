package com.jsnjwj.facade.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.request.BaseRequest;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.dto.ImportTeamDto;
import com.jsnjwj.facade.dto.SignSingleDto;
import com.jsnjwj.facade.dto.SignTeamDto;
import com.jsnjwj.facade.entity.TcSignSingle;
import com.jsnjwj.facade.entity.TcSignTeam;
import com.jsnjwj.facade.excel.TeamImportListener;
import com.jsnjwj.facade.manager.SignApplyManager;
import com.jsnjwj.facade.query.SignSingleListQuery;
import com.jsnjwj.facade.query.SignTeamListQuery;
import com.jsnjwj.facade.service.SignApplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
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
	public ApiResponse<?> fetchSinglePage(SignSingleListQuery query) {
		Page<SignSingleDto> page = new Page<>();
		List<SignSingleDto> list = signApplyManager.fetchSignSinglePage(query);
		page.setRecords(list);
		page.setTotal(signApplyManager.fetchSignSingleCount(query));
		return ApiResponse.success(page);
	}

	@Override
	public ApiResponse<?> fetchTeamPage(SignTeamListQuery query) {

		Page<TcSignTeam> page = signApplyManager.fetchSignTeamPage(query);

		return ApiResponse.success(page);
	}

	@Override
	public ApiResponse<?> importTeam(BaseRequest request, MultipartFile file) throws IOException {
		InputStream is = file.getInputStream();

		TeamImportListener userReadListener = new TeamImportListener(request.getGameId(), signApplyManager);
		EasyExcel.read(is, ImportTeamDto.class, userReadListener).sheet(0).headRowNumber(1).doRead();
		return ApiResponse.success();
	}

	@Override
	public ApiResponse<?> importSingle(BaseRequest request, MultipartFile file) {
		return null;
	}

}

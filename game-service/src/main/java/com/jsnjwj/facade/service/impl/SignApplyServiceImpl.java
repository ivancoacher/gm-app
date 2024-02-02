package com.jsnjwj.facade.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.request.BaseRequest;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.common.utils.ThreadLocalUtil;
import com.jsnjwj.facade.dto.SignTeamDto;
import com.jsnjwj.facade.easyexcel.upload.ImportSingleUploadDto;
import com.jsnjwj.facade.easyexcel.upload.ImportTeamUploadDto;
import com.jsnjwj.facade.dto.SignSingleDto;
import com.jsnjwj.facade.entity.GameGroupEntity;
import com.jsnjwj.facade.entity.GameItemEntity;
import com.jsnjwj.facade.entity.SignOrgEntity;
import com.jsnjwj.facade.entity.SignTeamEntity;
import com.jsnjwj.facade.excel.SingleImportListener;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
		list.forEach(signRecord -> {
			GameGroupEntity groupEntity = signApplyManager.getGroupById(signRecord.getGroupId());
			GameItemEntity itemEntity = signApplyManager.getItemById(signRecord.getItemId());
			SignTeamEntity teamEntity = signApplyManager.getTeamById(signRecord.getTeamId());
			if (Objects.nonNull(groupEntity)) {
				signRecord.setGroupName(groupEntity.getGroupName());
			}
			if (Objects.nonNull(itemEntity)) {
				signRecord.setItemName(itemEntity.getItemName());
			}

			if (Objects.nonNull(teamEntity)) {
				SignTeamDto teamDto = new SignTeamDto();
				teamDto.setTeamName(teamEntity.getTeamName());
				teamDto.setCoachName(teamEntity.getCoachName());
				teamDto.setCoachTel(teamEntity.getCoachTel());
				teamDto.setLeaderName(teamEntity.getLeaderName());
				teamDto.setLeaderTel(teamEntity.getLeaderTel());
				signRecord.setTeam(teamDto);

			}
		});
		page.setRecords(list);
		page.setTotal(signApplyManager.fetchSignSingleCount(query));
		return ApiResponse.success(page);
	}

	@Override
	public ApiResponse<?> fetchTeamPage(SignTeamListQuery query) {
		query.setGameId(ThreadLocalUtil.getCurrentGameId());
		Page<SignTeamEntity> page = signApplyManager.fetchSignTeamPage(query);
		List<SignTeamDto> teamDtoList = new ArrayList<>();
		if (!page.getRecords().isEmpty()) {
			for (SignTeamEntity entity : page.getRecords()) {
				SignTeamDto signTeamDto = new SignTeamDto();
				BeanUtil.copyProperties(entity, signTeamDto);
				Long orgId = entity.getOrgId();
				if (Objects.nonNull(orgId) && orgId > 0L) {
					SignOrgEntity orgEntity = signApplyManager.getSignOrgById(orgId);
					if (Objects.nonNull(orgEntity)) {
						signTeamDto.setOrgName(orgEntity.getOrgName());
					}
				}
				teamDtoList.add(signTeamDto);

			}
		}

		Page<SignTeamDto> response = new Page<>(query.getPage(), query.getLimit());
		response.setRecords(teamDtoList);
		response.setTotal(page.getTotal());

		return ApiResponse.success(response);
	}

	@Override
	public ApiResponse<?> fetchTeamData(SignTeamListQuery query) {
		query.setGameId(ThreadLocalUtil.getCurrentGameId());
		List<SignTeamEntity> page = signApplyManager.fetchSignTeamData(query);

		return ApiResponse.success(page);
	}

	@Override
	public ApiResponse<?> importTeam(BaseRequest request, MultipartFile file) throws IOException {
		InputStream is = file.getInputStream();

		TeamImportListener userReadListener = new TeamImportListener(request.getGameId(), signApplyManager);
		EasyExcel.read(is, ImportTeamUploadDto.class, userReadListener).sheet(0).headRowNumber(1).doRead();
		return ApiResponse.success();
	}

	@Override
	public ApiResponse<?> exportTeamDemo(BaseRequest baseRequest, MultipartFile file) throws IOException {
		return null;
	}

	@Override
	public ApiResponse<?> importSingle(Integer importType, MultipartFile file) {

		try {
			// 初始化监听器
			SingleImportListener singleImportListener = new SingleImportListener(ThreadLocalUtil.getCurrentGameId(),
					signApplyManager);
			// 解析数据
			EasyExcelFactory.read(file.getInputStream(), singleImportListener)
				.head(ImportSingleUploadDto.class)
				.headRowNumber(1)
				.sheet(0)
				.doReadSync();

		}
		catch (Exception e) {
			log.error("importSingle exception", e);
		}

		return ApiResponse.success();
	}

	@Override
	public ApiResponse<?> exportSingleDemo(BaseRequest baseRequest, MultipartFile file) {
		return null;
	}

}

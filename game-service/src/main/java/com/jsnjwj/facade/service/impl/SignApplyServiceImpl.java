package com.jsnjwj.facade.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
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
import com.jsnjwj.facade.entity.SignTeamEntity;
import com.jsnjwj.facade.excel.SingleImportListener;
import com.jsnjwj.facade.excel.TeamImportListener;
import com.jsnjwj.facade.manager.SignApplyManager;
import com.jsnjwj.facade.query.SignSingleListQuery;
import com.jsnjwj.facade.query.SignSingleProgramExportQuery;
import com.jsnjwj.facade.query.SignTeamListQuery;
import com.jsnjwj.facade.service.SignApplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
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
				teamDto.setCoachPhone(teamEntity.getCoachTel());
				teamDto.setLeaderName(teamEntity.getLeaderName());
				teamDto.setLeaderPhone(teamEntity.getLeaderTel());
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

		return ApiResponse.success(page);
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
	public ApiResponse<?> exportSignProgram(SignSingleProgramExportQuery request) {
		WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
		int cellCount = 10;

		// 计算每个单元格的宽度（假设以300 DPI打印）
		int paperWidthInPixels = 2480;  // A4纸的宽度（像素）
		int cellWidthInPixels = paperWidthInPixels / cellCount;

		// 转换像素为字符宽度
		int charWidth = cellWidthInPixels / 7;  // 假设使用默认的字体和字号，每个字符宽度为7像素

		// 自动换行
		contentWriteCellStyle.setWrapped(true);
		// 字体策略
		WriteFont contentWriteFont = new WriteFont();
		// 字体大小
		contentWriteFont.setFontHeightInPoints((short) 12);
		contentWriteCellStyle.setWriteFont(contentWriteFont);

		WriteCellStyle headWriteCellStyle = new WriteCellStyle();

		EasyExcel.write()
				//设置输出excel版本,不设置默认为xlsx
				.excelType(ExcelTypeEnum.XLS)
				.head(PilebodycheckMonthDto.class)
				//设置拦截器或自定义样式
				.registerWriteHandler(new MonthSheetWriteHandler())
				.registerWriteHandler(new HorizontalCellStyleStrategy(headWriteCellStyle,contentWriteCellStyle))
				.sheet("存量建筑垃圾堆体治理进度月报表")
				//设置默认样式及写入头信息开始的行数
				.useDefaultStyle(true).relativeHeadRowIndex(3)
				.doWrite();

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

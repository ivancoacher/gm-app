package com.jsnjwj.facade.service.v2.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson2.JSON;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.dto.ArrangeSessionInfoDto;
import com.jsnjwj.facade.dto.ArrangeSessionVo;
import com.jsnjwj.facade.entity.*;
import com.jsnjwj.facade.manager.ArrangeAreaSessionManager;
import com.jsnjwj.facade.manager.ArrangeSessionItemManager;
import com.jsnjwj.facade.manager.ArrangeSessionManager;
import com.jsnjwj.facade.manager.GameItemManager;
import com.jsnjwj.facade.query.session.GameGroupingSessionSetNumQuery;
import com.jsnjwj.facade.query.session.GameGroupingSessionSetQuery;
import com.jsnjwj.facade.service.v2.ArrangeSessionService;
import com.jsnjwj.facade.vo.session.SessionItemVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 场地安排
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ArrangeSessionServiceImpl implements ArrangeSessionService {

	private final ArrangeSessionManager arrangeSessionManager;

	private final ArrangeSessionItemManager arrangeSessionItemManager;

	private final ArrangeAreaSessionManager arrangeAreaSessionManager;

	private final GameItemManager gameItemManager;

	/**
	 * 创建场地
	 */
	@Override
	public ApiResponse<?> setSessionNum(GameGroupingSessionSetNumQuery query) {
		if (query.getSessionNum() <= 0)
			return ApiResponse.error("请输入正确的场地数");
		int courtNum = 1;
		// 重置所有场次数据
		arrangeSessionManager.resetCourt(query.getGameId());
		// 重置场次项目编排数据
		if (arrangeSessionItemManager.checkSessionItemExist(query.getGameId(), null)) {
			arrangeSessionItemManager.deleteBySessionId(query.getGameId(), null);
		}
		// 重置场地-场次编排数据
		if (arrangeAreaSessionManager.checkSessionItemExist(query.getGameId(), null)) {
			arrangeAreaSessionManager.deleteBySessionId(query.getGameId(), null);
		}
		List<GameSessionEntity> areas = new ArrayList<>();
		while (courtNum <= query.getSessionNum()) {
			GameSessionEntity area = new GameSessionEntity();
			area.setGameId(query.getGameId());
			area.setSessionName("场次" + courtNum);
			area.setSessionNo(courtNum);
			area.setStatus(1);
			areas.add(area);
			courtNum++;
		}
		arrangeSessionManager.saveSessionBatch(areas);
		return ApiResponse.success(true);
	}

	/**
	 * 新增单个场次
	 * @param gameId
	 * @return
	 */
	@Override
	public ApiResponse<?> addSession(Long gameId) {
		List<GameSessionEntity> sessionEntities = arrangeSessionManager.getListByGameId(gameId);
		int courtNum = 1;
		GameSessionEntity areaEntity = new GameSessionEntity();
		if (CollectionUtil.isEmpty(sessionEntities)) {
			areaEntity.setGameId(gameId);
			areaEntity.setSessionName("场次" + courtNum);
			areaEntity.setSessionNo(courtNum);
			areaEntity.setStatus(1);
		}
		else {
			courtNum = sessionEntities.get(sessionEntities.size() - 1).getSessionNo() + 1;
			areaEntity.setGameId(gameId);
			areaEntity.setSessionName("场次" + courtNum);
			areaEntity.setSessionNo(courtNum);
			areaEntity.setStatus(1);
		}
		arrangeSessionManager.saveSession(areaEntity);

		return ApiResponse.success();
	}

	/**
	 * 导出场次秩序册
	 * @param gameId
	 * @return
	 */
	@Override
	public ApiResponse<?> exportSession(Long gameId) {
		try {
			// 创建工作簿
			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet("场次秩序表");

			// 创建行
			Row row = sheet.createRow(0);

			// 设置字体样式
			Font font = workbook.createFont();
			font.setFontName("Arial");
			font.setFontHeightInPoints((short) 20);

			// 设置单元格样式
			CellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setAlignment(HorizontalAlignment.CENTER);
			cellStyle.setFont(font);

			// 设置行间距为1倍
			row.setHeightInPoints(37);

			// 设置垂直对齐方式为垂直居中
			cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyle.setWrapText(true); // 设置自动换行

			// 创建单元格
			Cell cell = row.createCell(0);
			cell.setCellValue("场次-秩序表");
			cell.setCellStyle(cellStyle);
			addMergedRegion(sheet, 0, 0, 0, 4);

			// 设置单元格宽度
			sheet.setColumnWidth(0, 12 * 256); // 设置列宽度

			// 设置整个表格的宽度适配 A4 纸张的宽度
			sheet.setFitToPage(true);
			PrintSetup printSetup = sheet.getPrintSetup();
			printSetup.setFitWidth((short) 1); // 将 Fit Width 设置为 1

			// 新增空白行
			addBlankRow(sheet, 1);

			List<GameSessionEntity> sessionEntityList = arrangeSessionManager.getListByGameId(gameId);
			if (CollectionUtil.isEmpty(sessionEntityList)) {
				throw new Exception("请先设置场次");
			}
			int i = 2;

			for (GameSessionEntity sessionEntity : sessionEntityList) {
				Row itemRow = sheet.createRow(i);
				itemRow.setHeightInPoints(24);
				Font groupFont = workbook.createFont();
				groupFont.setFontName("Arial");
				groupFont.setFontHeightInPoints((short) 15);

				CellStyle itemCellStyle = workbook.createCellStyle();
				itemCellStyle.setAlignment(HorizontalAlignment.CENTER);
				itemCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				itemCellStyle.setWrapText(true); // 设置自动换行
				itemCellStyle.setFont(groupFont);

				Font titleFont = workbook.createFont();
				titleFont.setFontName("Arial");
				titleFont.setFontHeightInPoints((short) 18);

				CellStyle titleCellStyle = workbook.createCellStyle();
				titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
				titleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				titleCellStyle.setWrapText(true); // 设置自动换行
				titleCellStyle.setFont(titleFont);

				Cell itemCell = itemRow.createCell(0);
				itemCell.setCellValue(sessionEntity.getSessionName());
				itemCell.setCellStyle(itemCellStyle);

				addMergedRegion(sheet, i, i, 0, 4);

				i++;
				Row titleItemRow = sheet.createRow(i);

				Cell titleCell1 = titleItemRow.createCell(0);
				titleCell1.setCellValue("出场编号");
				titleCell1.setCellStyle(titleCellStyle);

				Cell titleCell2 = titleItemRow.createCell(1);
				titleCell2.setCellValue("参赛选手");
				titleCell2.setCellStyle(titleCellStyle);

				Cell titleCell3 = titleItemRow.createCell(2);
				titleCell3.setCellValue("队名");
				titleCell3.setCellStyle(titleCellStyle);

				Cell titleCell4 = titleItemRow.createCell(3);
				titleCell4.setCellValue("单位");
				titleCell4.setCellStyle(titleCellStyle);

				Cell titleCell5 = titleItemRow.createCell(4);
				titleCell5.setCellValue("项目");
				titleCell5.setCellStyle(titleCellStyle);

				i++;
				// 查询该场次项目编排信息
				List<SessionItemVo> sessionItemVoList = arrangeSessionItemManager.fetchBySessionId(gameId,
						sessionEntity.getId());
				Font contentFont = workbook.createFont();
				contentFont.setFontName("Arial");
				contentFont.setFontHeightInPoints((short) 11);
				CellStyle contentCellStyle = workbook.createCellStyle();
				contentCellStyle.setAlignment(HorizontalAlignment.CENTER);
				contentCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				contentCellStyle.setWrapText(true); // 设置自动换行
				contentCellStyle.setFont(contentFont);
				for (SessionItemVo sessionItemVo : sessionItemVoList) {
					Row contentRow = sheet.createRow(i);

					Cell contentCell1 = contentRow.createCell(0);
					contentCell1.setCellValue(sessionItemVo.getSort());
					contentCell1.setCellStyle(contentCellStyle);

					Cell contentCell2 = titleItemRow.createCell(1);
					contentCell2.setCellValue("参赛选手");
					contentCell2.setCellStyle(contentCellStyle);

					Cell contentCell3 = titleItemRow.createCell(2);
					contentCell3.setCellValue("队名");
					contentCell3.setCellStyle(contentCellStyle);

					Cell contentCell4 = titleItemRow.createCell(3);
					contentCell4.setCellValue("单位");
					contentCell4.setCellStyle(contentCellStyle);

					Cell contentCell5 = titleItemRow.createCell(4);
					contentCell5.setCellValue("项目");
					contentCell5.setCellStyle(contentCellStyle);
					i++;
				}
				i++;

			}

			//
			// int i = 2;
			// List<SignSingleEntity> itemEntities =
			// signApplyManager.getSignItems(gameId);
			// if (CollUtil.isNotEmpty(itemEntities)) {
			// int orgCode = 1;
			//
			// for (SignSingleEntity signSingleEntity : itemEntities) {
			// // 填充group信息
			// i++;
			//
			// Long groupId = signSingleEntity.getGroupId();
			// Long itemId = signSingleEntity.getItemId();
			// // 填充报名信息 查询该group下，所有单位信息
			// List<SignSingleEntity> orgEnties =
			// signApplyManager.getOrgsByGroupIdAndItemId(gameId, groupId,
			// itemId);
			// if (CollUtil.isNotEmpty(orgEnties)) {
			// for (SignSingleEntity orgEntity : orgEnties) {
			// addBlankRow(sheet, i);
			// i++;
			// Row orgRow = sheet.createRow(i);
			// orgRow.setHeightInPoints(20);
			//
			// Font orgFont = workbook.createFont();
			// orgFont.setFontName("Arial");
			// orgFont.setFontHeightInPoints((short) 11);
			//
			// CellStyle orgCellStyle = workbook.createCellStyle();
			// orgCellStyle.setAlignment(HorizontalAlignment.CENTER);
			// orgCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			// orgCellStyle.setFont(orgFont);
			// orgCellStyle.setWrapText(true); // 设置自动换行
			//
			// // 编号
			// Cell orgCellCode = orgRow.createCell(0);
			//
			// String orgCodeStr = String.format("%03d", orgCode);
			// orgCellCode.setCellValue(orgCodeStr);
			// orgCellCode.setCellStyle(orgCellStyle);
			//
			// // 队伍名
			// Cell orgCellName = orgRow.createCell(1);
			// orgCellName.setCellValue(orgEntity.getOrgName());
			// orgCellName.setCellStyle(orgCellStyle);
			// addMergedRegion(sheet, i, i, 1, 8);
			//
			// orgCode++;
			// i++;
			//
			// String orgName = orgEntity.getOrgName();
			// // 查询每个组织下具体的报名信息
			// List<SignSingleEntity> singleEntities =
			// signApplyManager.getApplyByOrgAndGroupId(gameId,
			// groupId, orgName);
			//
			// if (CollUtil.isNotEmpty(singleEntities)) {
			// List<Long> teamIds = singleEntities.stream()
			// .map(SignSingleEntity::getTeamId)
			// .distinct()
			// .collect(Collectors.toList());
			// // 有队伍参赛的情况，生成领队和教练信息
			// if (CollUtil.isNotEmpty(teamIds)) {
			// List<SignTeamEntity> teamEntities = signApplyManager.getTeamsByIds(gameId,
			// teamIds);
			//
			// // 领队
			// List<String> leaderEntity = teamEntities.stream()
			// .map(SignTeamEntity::getLeaderName)
			// .collect(Collectors.toList());
			// String leaderEntityStr = String.join(",", leaderEntity);
			// addTeamRow(workbook, sheet, i, "领队", leaderEntityStr);
			// i++;
			// // 教练
			// List<String> coachEntity = teamEntities.stream()
			// .map(SignTeamEntity::getCoachName)
			// .collect(Collectors.toList());
			// String coachEntityStr = String.join(",", coachEntity);
			// addTeamRow(workbook, sheet, i, "教练", coachEntityStr);
			// i++;
			// }
			//
			// // 男运动员
			// List<SignSingleEntity> maleEntities = singleEntities.stream()
			// .filter(item -> item.getSex() == 1)
			// .collect(Collectors.toList());
			// if (CollUtil.isNotEmpty(maleEntities)) {
			// addPlayerTitleRow(workbook, sheet, i, "男运动员");
			// i++;
			// int maleCellIndex = 0;
			// for (SignSingleEntity signSingle : maleEntities) {
			// addPlayerContentRow(workbook, sheet, i, maleCellIndex,
			// signSingle.getName());
			// maleCellIndex++;
			// if (maleCellIndex >= 10) {
			// i++;
			// maleCellIndex = 0;
			// }
			// }
			// i++;
			// }
			// // 女运动员
			// List<SignSingleEntity> femaleEntities = singleEntities.stream()
			// .filter(item -> item.getSex() == 0)
			// .collect(Collectors.toList());
			// if (CollUtil.isNotEmpty(femaleEntities)) {
			// addPlayerTitleRow(workbook, sheet, i, "女运动员");
			// i++;
			// int femaleCellIndex = 0;
			// for (SignSingleEntity signSingle : femaleEntities) {
			// addPlayerContentRow(workbook, sheet, i, femaleCellIndex,
			// signSingle.getName());
			// femaleCellIndex++;
			// if (femaleCellIndex >= 10) {
			// i++;
			// femaleCellIndex = 0;
			// }
			// }
			// i++;
			// }
			// }
			//
			// }
			// addBlankRow(sheet, i);
			// i++;
			//
			// }
			//
			// }
			// }

			// 合并单元格
			// addMergedRegion(sheet, 0, 0, 0, 9);

			// 保存文件
			String filePath = "./file.xlsx";

			try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
				workbook.write(fileOut);
			}
			String fileName = "场次秩序表-" + gameId + ".xlsx";
			String fileUrl = updateToOss(filePath, fileName);
			log.info("Excel文件已生成！");

			return ApiResponse.success(fileUrl);

		}
		catch (Exception e) {
			log.error("export error", e);
			return ApiResponse.error("导出失败");

		}

	}

	/**
	 * 上传导出文件到oss
	 * @param fileName String
	 * @param orgName String
	 * @return String
	 * @throws IOException IOException
	 */
	private String updateToOss(String fileName, String orgName) throws IOException {

		String endpoint = "https://oss-cn-beijing.aliyuncs.com";
		// 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。
		// 这里填写上面获取的 accessKeyId、 accessKeySecret
		String accessKeyId = "LTAI5tGUes1rJF4GMVDUGrWk";
		String accessKeySecret = "HgBCLqvx31QlXMetWRyPyZWGAJcofi";

		OSS client = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

		// 2. 通过client发起上传文件的请求
		String bulkName = "dynamicycle";
		// String key = UUID.randomUUID().toString() + "git-bash.pdf";
		// //文件的唯一标识，通常是不重复的文件名

		InputStream in = Files.newInputStream(Paths.get(fileName));

		// 上传文件到指定的存储空间（bucketName）并将其保存为指定的文件名称（key）。
		PutObjectResult resul = client.putObject(bulkName, orgName, in);
		log.info(JSON.toJSONString(resul));
		// return resul.getResponse().getUri();
		// return "https://shfamily-school.oss-cn-shanghai.aliyuncs.com/"+key;
		return "https://doc.dynamicycle.com/" + orgName;

	}

	/**
	 * 合并单元格
	 * @param sheet Sheet
	 * @param startRow 开始行
	 * @param endRow 结束行
	 * @param startCol 开始列
	 * @param endCol 结束列
	 */
	private void addMergedRegion(Sheet sheet, int startRow, int endRow, int startCol, int endCol) {
		sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, startCol, endCol));
	}

	private void addBlankRow(Sheet sheet, int i, int columns) {
		// 创建行
		Row teamRow = sheet.createRow(i);
		teamRow.setHeightInPoints(20);

		// 新增单元格
		teamRow.createCell(0);

		addMergedRegion(sheet, i, i, 0, columns);
	}

	private void addBlankRow(Sheet sheet, int i) {
		// 创建行
		Row teamRow = sheet.createRow(i);
		teamRow.setHeightInPoints(20);

		// 新增单元格
		teamRow.createCell(0);

		addMergedRegion(sheet, i, i, 0, 4);
	}

	/**
	 * 删除单个场次
	 * @param query
	 * @return
	 */
	@Override
	public ApiResponse<?> deleteSession(GameGroupingSessionSetQuery query) {
		Long sessionId = query.getSessionId();
		Long gameId = query.getGameId();
		// 校验该场次下，是否存在已排项目
		List<GameSessionItemEntity> gameSessionItemEntities = arrangeSessionItemManager.fetchListBySessionId(gameId,
				sessionId);
		if (CollectionUtil.isNotEmpty(gameSessionItemEntities)) {
			arrangeSessionItemManager.deleteBySessionId(gameId, sessionId);
		}
		// 删除场次
		arrangeSessionManager.deleteById(gameId, sessionId);

		return ApiResponse.success();
	}

	/**
	 * 保存场地
	 * @param query
	 * @return
	 */
	@Override
	public ApiResponse<Boolean> saveSession(GameGroupingSessionSetQuery query) {
		Long gameId = query.getGameId();
		Long sessionId = query.getSessionId();
		if (Objects.isNull(sessionId)) {
			return ApiResponse.error("该场次不存在");
		}
		GameSessionEntity session = arrangeSessionManager.getBySessionId(sessionId);
		if (Objects.isNull(session)) {
			return ApiResponse.error("该场次不存在");
		}

		session.setSessionName(query.getSessionName());
		session.setSessionNo(query.getSessionNo());
		session.setStatus(query.getStatus());
		session.setCreatedAt(new Date());
		session.setUpdatedAt(new Date());
		arrangeSessionManager.saveSession(session);

		GameSessionSettingEntity settingEntity = arrangeSessionManager.getSettingBySessionId(gameId, sessionId);
		if (Objects.isNull(settingEntity)) {
			settingEntity = new GameSessionSettingEntity();
			settingEntity.setSessionId(sessionId);
			settingEntity.setGameId(gameId);
			settingEntity.setCreatedAt(new Date());
		}
		GameGroupingSessionSetQuery.SessionSettingVo settingVo = query.getSessionSetting();
		settingEntity.setOrgMin(settingVo.getOrgRange().stream().sorted().collect(Collectors.toList()).get(0));
		settingEntity.setOrgMax(settingVo.getOrgRange().stream().sorted().collect(Collectors.toList()).get(1));
		settingEntity.setTeamMin(settingVo.getTeamRange().stream().sorted().collect(Collectors.toList()).get(0));
		settingEntity.setTeamMax(settingVo.getTeamRange().stream().sorted().collect(Collectors.toList()).get(1));
		settingEntity.setSingleMin(settingVo.getSingleRange().stream().sorted().collect(Collectors.toList()).get(0));
		settingEntity.setSingleMax(settingVo.getSingleRange().stream().sorted().collect(Collectors.toList()).get(1));
		arrangeSessionManager.saveSessionSetting(settingEntity);

		return ApiResponse.success(true);
	}

	/**
	 * 获取所有场地对应场次信息
	 * @param gameId
	 * @return
	 */
	@Override
	public ApiResponse<List<ArrangeSessionVo>> getSessions(Long gameId) {
		// 获取所有场次信息
		List<GameSessionEntity> sessionList = arrangeSessionManager.getListByGameId(gameId);

		if (CollectionUtil.isEmpty(sessionList)) {
			return ApiResponse.success(new ArrayList<>());
		}
		List<ArrangeSessionVo> response;

		response = sessionList.stream().map(session -> {
			ArrangeSessionVo arrangeSessionVo = new ArrangeSessionVo();
			arrangeSessionVo.setSessionId(session.getId());
			arrangeSessionVo.setSessionName(session.getSessionName());
			arrangeSessionVo.setSessionNo(session.getSessionNo());

			// 校验该场次下，是否存在已排项目
			List<SessionItemVo> sessionItemList = arrangeSessionItemManager.fetchBySessionId(gameId, session.getId());
			if (CollectionUtil.isNotEmpty(sessionItemList)) {

				arrangeSessionVo.setItemNum(sessionItemList.size());

				arrangeSessionVo
					.setItemList(sessionItemList.size() > 3 ? sessionItemList.subList(0, 3) : sessionItemList);

			}

			GameSessionSettingEntity settingEntity = arrangeSessionManager.getSettingBySessionId(gameId,
					session.getId());
			if (Objects.nonNull(settingEntity)) {
				ArrangeSessionVo.SessionSettingVo settingVo = new ArrangeSessionVo.SessionSettingVo();
				settingVo.setOrgRange(Arrays.asList(settingEntity.getOrgMin(), settingEntity.getOrgMax()));
				settingVo.setTeamRange(Arrays.asList(settingEntity.getTeamMin(), settingEntity.getTeamMax()));
				settingVo.setSingleRange(Arrays.asList(settingEntity.getSingleMin(), settingEntity.getSingleMax()));
				arrangeSessionVo.setSessionSetting(settingVo);
			}
			return arrangeSessionVo;
		}).collect(Collectors.toList());

		return ApiResponse.success(response);
	}

	@Override
	public ApiResponse<ArrangeSessionInfoDto> getSessionInfo(Long gameId) {
		Long totalNum = gameItemManager.countByGameId(gameId);
		Long arrangedNum = arrangeSessionItemManager.countByGameId(gameId);
		ArrangeSessionInfoDto arrangeSessionInfoDto = new ArrangeSessionInfoDto();
		arrangeSessionInfoDto.setTotalItemCount(totalNum.intValue());
		arrangeSessionInfoDto.setArrangedItemCount(arrangedNum.intValue());
		return ApiResponse.success(arrangeSessionInfoDto);
	}

}

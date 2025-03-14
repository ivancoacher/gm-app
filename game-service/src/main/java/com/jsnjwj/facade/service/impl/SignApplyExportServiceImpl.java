package com.jsnjwj.facade.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson2.JSON;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.entity.GameGroupEntity;
import com.jsnjwj.facade.entity.GameItemEntity;
import com.jsnjwj.facade.entity.SignSingleEntity;
import com.jsnjwj.facade.entity.SignTeamEntity;
import com.jsnjwj.facade.enums.SignExportTypeEnum;
import com.jsnjwj.facade.manager.SignApplyManager;
import com.jsnjwj.facade.query.SignSingleProgramExportQuery;
import com.jsnjwj.facade.service.SignApplyExportService;
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
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 合同比对service
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SignApplyExportServiceImpl implements SignApplyExportService {

	private final static String SHEET_NAME = "sheet1";

	private final SignApplyManager signApplyManager;

	private final Map<String, Long> groupCountMap = new HashMap<>();

	private final Map<String, Integer> groupCountKeyMap = new HashMap<>();

	@Override
	public ApiResponse<?> exportSignProgram(SignSingleProgramExportQuery request) {
		Long gameId = request.getGameId();
		String exportType = request.getType();
		SignExportTypeEnum typeEnum = Enum.valueOf(SignExportTypeEnum.class, exportType.toUpperCase());
		switch (typeEnum) {
			case TYPE_ITEM_ORG:
				return exportByItemAndOrg(gameId);
			case TYPE_GROUP_ORG:
				return exportByGroupAndOrg(gameId);
			case TYPE_TEAM:
				return exportByTeam(gameId);
			case TYPE_GROUP_TEAM:
				return exportByGroupAndTeam(gameId);
			case TYPE_ITEM_TEAM:
				return exportByItemAndTeam(gameId);
			case TYPE_PROJECT:
				return exportByProject(gameId);
			case TYPE_PROJECT_PLAYER:
				return exportByProjectPlayer(gameId);
			case TYPE_PLAYER_COUNTING:
				return exportByPlayerCounting(gameId);
			case TYPE_PLAYER_TIMES:
				return exportByPlayerTimes(gameId);
			default:
				return exportByOrg(gameId);
		}

	}

	@Override
	public ApiResponse<?> exportSignProjectProgram(SignSingleProgramExportQuery request) {
		return exportByItemAndOrg(request.getGameId());

	}

	/**
	 * 根据组别导出
	 * @param gameId 比赛编号
	 * @return ApiResponse
	 */
	private ApiResponse<?> exportByGroupAndTeam(Long gameId) {
		try {
			// 创建工作簿
			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet(SHEET_NAME);

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

			// 创建单元格
			Cell cell = row.createCell(0);
			cell.setCellValue("秩序册-组别");
			cell.setCellStyle(cellStyle);

			// 设置单元格宽度
			sheet.setColumnWidth(0, 12 * 256); // 设置列宽度

			// 设置整个表格的宽度适配 A4 纸张的宽度
			sheet.setFitToPage(true);
			PrintSetup printSetup = sheet.getPrintSetup();
			printSetup.setFitWidth((short) 1); // 将 Fit Width 设置为 1

			// 新增空白行
			addBlankRow(sheet, 1);

			int i = 2;
			// List<SignSingleEntity> groupEntities =
			// signApplyManager.getSignGroups(gameId);
			List<GameGroupEntity> groupEntities = signApplyManager.getGroupList(gameId);
			if (CollUtil.isNotEmpty(groupEntities)) {
				int orgCode = 1;

				for (GameGroupEntity signSingleEntity : groupEntities) {
					Long groupId = signSingleEntity.getId();
					// 填充group信息
					GameGroupEntity groupEntity = signApplyManager.getGroupById(groupId);
					Row groupRow = sheet.createRow(i);
					groupRow.setHeightInPoints(24);

					Font groupFont = workbook.createFont();
					groupFont.setFontName("Arial");
					groupFont.setFontHeightInPoints((short) 15);

					groupRow.setHeightInPoints(24);

					CellStyle groupCellStyle = workbook.createCellStyle();
					groupCellStyle.setAlignment(HorizontalAlignment.CENTER);
					groupCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

					groupCellStyle.setFont(groupFont);

					Cell groupCell = groupRow.createCell(0);
					groupCell.setCellValue(groupEntity.getGroupName());
					groupCellStyle.setWrapText(true); // 设置自动换行

					groupCell.setCellStyle(groupCellStyle);

					addMergedRegion(sheet, i, i, 0, 9);

					i++;

					// 填充报名信息 查询该group下，所有单位信息
					List<SignSingleEntity> teamEnties = signApplyManager.getTeamByGroupId(gameId, groupId);
					if (CollUtil.isNotEmpty(teamEnties)) {
						teamEnties = teamEnties.stream()
							.sorted(Comparator.comparing(SignSingleEntity::getTeamId).reversed())
							.collect(Collectors.toList());

						for (SignSingleEntity teamEntity : teamEnties) {
							List<SignSingleEntity> teamSignEnties = signApplyManager.getApplyByGroupIdAndTeamId(gameId,
									groupId, teamEntity.getTeamId());

							if (teamEntity.getTeamId() > 0) {
								SignTeamEntity signTeamEntity = signApplyManager.getTeamById(teamEntity.getTeamId());

								addBlankRow(sheet, i);
								i++;
								Row orgRow = sheet.createRow(i);
								orgRow.setHeightInPoints(20);

								Font orgFont = workbook.createFont();
								orgFont.setFontName("Arial");
								orgFont.setFontHeightInPoints((short) 11);

								CellStyle orgCellStyle = workbook.createCellStyle();
								orgCellStyle.setAlignment(HorizontalAlignment.CENTER);
								orgCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
								orgCellStyle.setFont(orgFont);
								orgCellStyle.setWrapText(true); // 设置自动换行

								// 编号
								Cell orgCellCode = orgRow.createCell(0);

								String orgCodeStr = String.format("%03d", orgCode);
								orgCellCode.setCellValue(orgCodeStr);
								orgCellCode.setCellStyle(orgCellStyle);

								// 队伍名
								Cell orgCellName = orgRow.createCell(1);
								orgCellName.setCellValue(signTeamEntity.getTeamName());
								orgCellName.setCellStyle(orgCellStyle);
								addMergedRegion(sheet, i, i, 1, 8);

								orgCode++;
								i++;

								List<Long> teamIds = teamSignEnties.stream()
									.map(SignSingleEntity::getTeamId)
									.distinct()
									.collect(Collectors.toList());
								// 有队伍参赛的情况，生成领队和教练信息
								if (CollUtil.isNotEmpty(teamIds)) {
									List<SignTeamEntity> teamEntities = signApplyManager.getTeamsByIds(gameId, teamIds);

									// 领队
									List<String> leaderEntity = teamEntities.stream()
										.map(SignTeamEntity::getLeaderName)
										.collect(Collectors.toList());
									String leaderEntityStr = String.join(",", leaderEntity);
									addTeamRow(workbook, sheet, i, "领队", leaderEntityStr);
									i++;
									// 教练
									List<String> coachEntity = teamEntities.stream()
										.map(SignTeamEntity::getCoachName)
										.collect(Collectors.toList());
									String coachEntityStr = String.join(",", coachEntity);
									addTeamRow(workbook, sheet, i, "教练", coachEntityStr);
									i++;
								}

								// 男运动员
								List<SignSingleEntity> maleEntities = teamSignEnties.stream()
									.filter(item -> item.getSex() == 1)
									.collect(Collectors.toList());
								if (CollUtil.isNotEmpty(maleEntities)) {
									addPlayerTitleRow(workbook, sheet, i, "男运动员");
									i++;
									int maleCellIndex = 0;
									for (SignSingleEntity signSingle : maleEntities) {
										addPlayerContentRow(workbook, sheet, i, maleCellIndex, signSingle.getName());
										maleCellIndex++;
										if (maleCellIndex >= 10) {
											i++;
											maleCellIndex = 0;
										}
									}
									i++;
								}
								// 女运动员
								List<SignSingleEntity> femaleEntities = teamSignEnties.stream()
									.filter(item -> item.getSex() == 0)
									.collect(Collectors.toList());
								if (CollUtil.isNotEmpty(femaleEntities)) {
									addPlayerTitleRow(workbook, sheet, i, "女运动员");
									i++;
									int femaleCellIndex = 0;
									for (SignSingleEntity signSingle : femaleEntities) {
										addPlayerContentRow(workbook, sheet, i, femaleCellIndex, signSingle.getName());
										femaleCellIndex++;
										if (femaleCellIndex >= 10) {
											i++;
											femaleCellIndex = 0;
										}
									}
									i++;
								}
							}
							else {
								addBlankRow(sheet, i);
								i++;
								Row orgRow = sheet.createRow(i);
								orgRow.setHeightInPoints(20);

								Font orgFont = workbook.createFont();
								orgFont.setFontName("Arial");
								orgFont.setFontHeightInPoints((short) 11);

								CellStyle orgCellStyle = workbook.createCellStyle();
								orgCellStyle.setAlignment(HorizontalAlignment.CENTER);
								orgCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
								orgCellStyle.setFont(orgFont);
								orgCellStyle.setWrapText(true); // 设置自动换行

								// 编号
								Cell orgCellCode = orgRow.createCell(0);

								String orgCodeStr = String.format("%03d", orgCode);
								orgCellCode.setCellValue(orgCodeStr);
								orgCellCode.setCellStyle(orgCellStyle);

								// 队伍名
								Cell orgCellName = orgRow.createCell(1);
								orgCellName.setCellValue("个人选手");
								orgCellName.setCellStyle(orgCellStyle);
								addMergedRegion(sheet, i, i, 1, 8);

								orgCode++;
								i++;

								// 男运动员
								List<SignSingleEntity> maleEntities = teamSignEnties.stream()
									.filter(item -> item.getSex() == 1)
									.collect(Collectors.toList());
								if (CollUtil.isNotEmpty(maleEntities)) {
									addPlayerTitleRow(workbook, sheet, i, "男运动员");
									i++;
									int maleCellIndex = 0;
									for (SignSingleEntity signSingle : maleEntities) {
										addPlayerContentRow(workbook, sheet, i, maleCellIndex, signSingle.getName());
										maleCellIndex++;
										if (maleCellIndex >= 10) {
											i++;
											maleCellIndex = 0;
										}
									}
									i++;
								}
								// 女运动员
								List<SignSingleEntity> femaleEntities = teamSignEnties.stream()
									.filter(item -> item.getSex() == 0)
									.collect(Collectors.toList());
								if (CollUtil.isNotEmpty(femaleEntities)) {
									addPlayerTitleRow(workbook, sheet, i, "女运动员");
									i++;
									int femaleCellIndex = 0;
									for (SignSingleEntity signSingle : femaleEntities) {
										addPlayerContentRow(workbook, sheet, i, femaleCellIndex, signSingle.getName());
										femaleCellIndex++;
										if (femaleCellIndex >= 10) {
											i++;
											femaleCellIndex = 0;
										}
									}
									i++;
								}
								addBlankRow(sheet, i);
								i++;
							}

						}
						addBlankRow(sheet, i);
						i++;

					}

				}
			}

			// 合并单元格
			addMergedRegion(sheet, 0, 0, 0, 9);

			// 保存文件
			String filePath = "./file.xlsx";

			try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
				workbook.write(fileOut);
			}
			String fileName = "秩序表-组别（队伍）-" + gameId + ".xlsx";
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
	 * 根据组别导出
	 * @param gameId
	 * @return
	 */
	private ApiResponse<?> exportByGroupAndOrg(Long gameId) {
		try {
			// 创建工作簿
			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet(SHEET_NAME);

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

			// 创建单元格
			Cell cell = row.createCell(0);
			cell.setCellValue("秩序册-组别");
			cell.setCellStyle(cellStyle);

			// 设置单元格宽度
			sheet.setColumnWidth(0, 12 * 256); // 设置列宽度

			// 设置整个表格的宽度适配 A4 纸张的宽度
			sheet.setFitToPage(true);
			PrintSetup printSetup = sheet.getPrintSetup();
			printSetup.setFitWidth((short) 1); // 将 Fit Width 设置为 1

			// 新增空白行
			addBlankRow(sheet, 1);

			int i = 2;
			List<GameGroupEntity> groupEntities = signApplyManager.getGroupList(gameId);

			if (CollUtil.isNotEmpty(groupEntities)) {
				int orgCode = 1;

				for (GameGroupEntity signSingleEntity : groupEntities) {
					Long groupId = signSingleEntity.getId();
					// 填充group信息
					GameGroupEntity groupEntity = signApplyManager.getGroupById(groupId);
					Row groupRow = sheet.createRow(i);
					groupRow.setHeightInPoints(24);

					Font groupFont = workbook.createFont();
					groupFont.setFontName("Arial");
					groupFont.setFontHeightInPoints((short) 15);

					groupRow.setHeightInPoints(24);

					CellStyle groupCellStyle = workbook.createCellStyle();
					groupCellStyle.setAlignment(HorizontalAlignment.CENTER);
					groupCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

					groupCellStyle.setFont(groupFont);

					Cell groupCell = groupRow.createCell(0);
					groupCell.setCellValue(groupEntity.getGroupName());
					groupCellStyle.setWrapText(true); // 设置自动换行

					groupCell.setCellStyle(groupCellStyle);

					addMergedRegion(sheet, i, i, 0, 9);

					i++;

					// 填充报名信息 查询该group下，所有单位信息
					List<SignSingleEntity> orgEnties = signApplyManager.getOrgsByGroupId(gameId, groupId);
					if (CollUtil.isNotEmpty(orgEnties)) {
						for (SignSingleEntity orgEntity : orgEnties) {
							addBlankRow(sheet, i);
							i++;
							Row orgRow = sheet.createRow(i);
							orgRow.setHeightInPoints(20);

							Font orgFont = workbook.createFont();
							orgFont.setFontName("Arial");
							orgFont.setFontHeightInPoints((short) 11);

							CellStyle orgCellStyle = workbook.createCellStyle();
							orgCellStyle.setAlignment(HorizontalAlignment.CENTER);
							orgCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
							orgCellStyle.setFont(orgFont);
							orgCellStyle.setWrapText(true); // 设置自动换行

							// 编号
							Cell orgCellCode = orgRow.createCell(0);

							String orgCodeStr = String.format("%03d", orgCode);
							orgCellCode.setCellValue(orgCodeStr);
							orgCellCode.setCellStyle(orgCellStyle);

							// 队伍名
							Cell orgCellName = orgRow.createCell(1);
							orgCellName.setCellValue(orgEntity.getOrgName());
							orgCellName.setCellStyle(orgCellStyle);
							addMergedRegion(sheet, i, i, 1, 8);

							orgCode++;
							i++;

							String orgName = orgEntity.getOrgName();
							// 查询每个组织下具体的报名信息
							List<SignSingleEntity> singleEntities = signApplyManager.getApplyByOrgAndGroupId(gameId,
									groupId, orgName);

							if (CollUtil.isNotEmpty(singleEntities)) {
								List<Long> teamIds = singleEntities.stream()
									.map(SignSingleEntity::getTeamId)
									.distinct()
									.collect(Collectors.toList());
								// 有队伍参赛的情况，生成领队和教练信息
								if (CollUtil.isNotEmpty(teamIds)) {
									List<SignTeamEntity> teamEntities = signApplyManager.getTeamsByIds(gameId, teamIds);

									// 领队
									List<String> leaderEntity = teamEntities.stream()
										.map(SignTeamEntity::getLeaderName)
										.collect(Collectors.toList());
									String leaderEntityStr = String.join(",", leaderEntity);
									addTeamRow(workbook, sheet, i, "领队", leaderEntityStr);
									i++;
									// 教练
									List<String> coachEntity = teamEntities.stream()
										.map(SignTeamEntity::getCoachName)
										.collect(Collectors.toList());
									String coachEntityStr = String.join(",", coachEntity);
									addTeamRow(workbook, sheet, i, "教练", coachEntityStr);
									i++;
								}

								// 男运动员
								List<SignSingleEntity> maleEntities = singleEntities.stream()
									.filter(item -> item.getSex() == 1)
									.collect(Collectors.toList());
								if (CollUtil.isNotEmpty(maleEntities)) {
									addPlayerTitleRow(workbook, sheet, i, "男运动员");
									i++;
									int maleCellIndex = 0;
									maleEntities = new ArrayList<>(maleEntities.stream()
										.collect(Collectors.toMap(SignSingleEntity::getName, Function.identity(),
												(existing, replacement) -> existing))
										.values());
									for (SignSingleEntity signSingle : maleEntities) {
										addPlayerContentRow(workbook, sheet, i, maleCellIndex, signSingle.getName());
										maleCellIndex++;
										if (maleCellIndex >= 10) {
											i++;
											maleCellIndex = 0;
										}
									}
									i++;
								}
								// 女运动员
								List<SignSingleEntity> femaleEntities = singleEntities.stream()
									.filter(item -> item.getSex() == 0)
									.collect(Collectors.toList());
								if (CollUtil.isNotEmpty(femaleEntities)) {
									femaleEntities = new ArrayList<>(femaleEntities.stream()
										.collect(Collectors.toMap(SignSingleEntity::getName, Function.identity(),
												(existing, replacement) -> existing))
										.values());
									addPlayerTitleRow(workbook, sheet, i, "女运动员");
									i++;
									int femaleCellIndex = 0;
									for (SignSingleEntity signSingle : femaleEntities) {
										addPlayerContentRow(workbook, sheet, i, femaleCellIndex, signSingle.getName());
										femaleCellIndex++;
										if (femaleCellIndex >= 10) {
											i++;
											femaleCellIndex = 0;
										}
									}
									i++;
								}
							}

						}
						addBlankRow(sheet, i);
						i++;

					}

				}
			}

			// 合并单元格
			addMergedRegion(sheet, 0, 0, 0, 9);

			// 保存文件
			String filePath = "./file.xlsx";

			try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
				workbook.write(fileOut);
			}
			String fileName = "秩序表-组别-" + gameId + ".xlsx";
			String fileUrl = updateToOss(filePath, fileName);
			log.info("Excel文件已生成！");

			return ApiResponse.success(fileUrl);

		}
		catch (Exception e) {
			log.error("export error", e);
			return ApiResponse.error("导出失败");

		}
	}

	private void addBlankRow(Sheet sheet, int i) {
		// 创建行
		Row teamRow = sheet.createRow(i);
		teamRow.setHeightInPoints(20);

		// 新增单元格
		teamRow.createCell(0);

		addMergedRegion(sheet, i, i, 0, 9);
	}

	private void addBlankRow(Sheet sheet, int i, int columns) {
		// 创建行
		Row teamRow = sheet.createRow(i);
		teamRow.setHeightInPoints(20);

		// 新增单元格
		teamRow.createCell(0);

		addMergedRegion(sheet, i, i, 0, columns);
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
	 * 绘制运动员信息
	 * @param workbook Workbook
	 * @param sheet Sheet
	 * @param i int
	 * @param titleName 标题名
	 */
	private void addPlayerTitleRow(Workbook workbook, Sheet sheet, int i, String titleName) {
		Row teamRow = sheet.createRow(i);
		teamRow.setHeightInPoints(20);

		Font orgFont = workbook.createFont();
		orgFont.setFontName("Arial");
		orgFont.setFontHeightInPoints((short) 11);

		CellStyle teamCellStyle = workbook.createCellStyle();
		teamCellStyle.setAlignment(HorizontalAlignment.LEFT);
		teamCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		teamCellStyle.setFont(orgFont);

		// title
		Cell titleCellCode = teamRow.createCell(0);

		titleCellCode.setCellValue(titleName + ":");
		titleCellCode.setCellStyle(teamCellStyle);

		addMergedRegion(sheet, i, i, 0, 9);

	}

	/**
	 * 设置队员信息单元格
	 * @param workbook Workbook
	 * @param sheet Sheet
	 * @param rowId 行数
	 * @param cellId 列数
	 * @param columnContent 内容
	 */
	private void addPlayerContentRow(Workbook workbook, Sheet sheet, int rowId, int cellId, String columnContent) {
		Row playerRow = sheet.getRow(rowId); // 获取当前行
		if (playerRow == null) {
			playerRow = sheet.createRow(rowId);
		}
		playerRow.setHeightInPoints(20);

		Font orgFont = workbook.createFont();
		orgFont.setFontName("Arial");
		orgFont.setFontHeightInPoints((short) 11);

		CellStyle playerCellStyle = workbook.createCellStyle();
		playerCellStyle.setAlignment(HorizontalAlignment.CENTER);
		playerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		playerCellStyle.setFont(orgFont);

		// 编号
		Cell titleCellCode = playerRow.createCell(cellId);

		titleCellCode.setCellValue(columnContent);
		playerCellStyle.setWrapText(true); // 设置自动换行

		titleCellCode.setCellStyle(playerCellStyle);

	}

	/**
	 * 绘制教练+领队信息
	 * @param workbook
	 * @param sheet
	 * @param i
	 * @param columnTitleName
	 * @param columnContent
	 */
	private void addTeamRow(Workbook workbook, Sheet sheet, int i, String columnTitleName, String columnContent) {
		Row teamRow = sheet.createRow(i);
		teamRow.setHeightInPoints(20);

		Font orgFont = workbook.createFont();
		orgFont.setFontName("Arial");
		orgFont.setFontHeightInPoints((short) 11);

		CellStyle teamCellStyle = workbook.createCellStyle();
		teamCellStyle.setAlignment(HorizontalAlignment.LEFT);
		teamCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		teamCellStyle.setFont(orgFont);
		teamCellStyle.setWrapText(true); // 设置自动换行

		// 编号
		Cell titleCellCode = teamRow.createCell(0);

		titleCellCode.setCellValue(columnTitleName + ":");
		titleCellCode.setCellStyle(teamCellStyle);

		// 教练或者领队
		Cell contentCellName = teamRow.createCell(1);
		contentCellName.setCellValue(columnContent);
		contentCellName.setCellStyle(teamCellStyle);
		addMergedRegion(sheet, i, i, 1, 9);

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

	/**
	 * 导出项目人数统计表
	 * @param gameId 比赛编号
	 * @return
	 */
	private ApiResponse<?> exportByProjectPlayer(Long gameId) {
		try {
			// 创建工作簿
			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet(SHEET_NAME);

			Row titleRow = sheet.createRow(0);
			Font titleFont = workbook.createFont();
			titleFont.setFontName("Arial");
			titleFont.setFontHeightInPoints((short) 14);

			CellStyle titleCellStyle = workbook.createCellStyle();
			titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
			titleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			titleCellStyle.setWrapText(true); // 设置自动换行
			titleCellStyle.setFont(titleFont);

			Cell itemCell1 = titleRow.createCell(0);
			itemCell1.setCellValue("序号");
			itemCell1.setCellStyle(titleCellStyle);
			sheet.autoSizeColumn(0);

			Cell itemCell2 = titleRow.createCell(1);
			itemCell2.setCellValue("组别名称");
			itemCell2.setCellStyle(titleCellStyle);
			sheet.autoSizeColumn(1);

			Cell itemCell3 = titleRow.createCell(2);
			itemCell3.setCellValue("项目名称");
			itemCell3.setCellStyle(titleCellStyle);
			sheet.autoSizeColumn(2);

			Cell itemCell4 = titleRow.createCell(3);
			itemCell4.setCellValue("单位");
			itemCell4.setCellStyle(titleCellStyle);
			sheet.autoSizeColumn(3);

			Cell itemCell5 = titleRow.createCell(4);
			itemCell5.setCellValue("队伍");
			itemCell5.setCellStyle(titleCellStyle);
			sheet.autoSizeColumn(4);

			int i = 1;
			List<SignSingleEntity> itemEntities = signApplyManager.getSignTeams(gameId);
			if (CollUtil.isNotEmpty(itemEntities)) {
				int orgCode = 1;

				for (SignSingleEntity signSingleEntity : itemEntities) {

					// 填充group信息
					GameGroupEntity groupEntity = signApplyManager.getGroupById(signSingleEntity.getGroupId());
					GameItemEntity itemEntity = signApplyManager.getItemById(signSingleEntity.getItemId());

					Row itemRow = sheet.createRow(i);
					itemRow.setHeightInPoints(24);

					Font groupFont = workbook.createFont();
					groupFont.setFontName("Arial");
					groupFont.setFontHeightInPoints((short) 12);

					CellStyle itemCellStyle = workbook.createCellStyle();
					itemCellStyle.setAlignment(HorizontalAlignment.CENTER);
					itemCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
					itemCellStyle.setWrapText(true); // 设置自动换行
					itemCellStyle.setFont(groupFont);

					// 序号
					Cell srotNumCell = itemRow.createCell(0);
					srotNumCell.setCellValue(orgCode);
					srotNumCell.setCellStyle(itemCellStyle);
					orgCode++;
					// 所属组别
					Cell groupCell = itemRow.createCell(1);
					groupCell.setCellValue(groupEntity.getGroupName());
					groupCell.setCellStyle(itemCellStyle);
					sheet.autoSizeColumn(1);

					// 项目名称
					Cell itemCell = itemRow.createCell(2);
					itemCell.setCellValue(itemEntity.getItemName());
					itemCell.setCellStyle(itemCellStyle);
					sheet.autoSizeColumn(2);

					Long itemId = signSingleEntity.getItemId();
					// 单位
					List<SignSingleEntity> orgEntities = signApplyManager.getTeamByGroupIdAndItemId(gameId,
							groupEntity.getId(), itemId);
					Cell orgNumsCell = itemRow.createCell(3);
					orgNumsCell.setCellValue(orgEntities.get(0).getOrgName());
					orgNumsCell.setCellStyle(itemCellStyle);
					sheet.autoSizeColumn(3);

					// 单位
					Long teamId = signSingleEntity.getTeamId();
					SignTeamEntity teamEntity = signApplyManager.getTeamById(teamId);
					Cell teamCell = itemRow.createCell(4);
					teamCell.setCellValue(teamEntity.getTeamName());
					teamCell.setCellStyle(itemCellStyle);
					sheet.autoSizeColumn(4);

					// 获取报名用户信息
					List<SignSingleEntity> singleEntities = signApplyManager.getApplyByTeam(gameId, teamId);

					int playerCode = 1;
					if (CollUtil.isNotEmpty(singleEntities)) {
						singleEntities = new ArrayList<>(singleEntities.stream()
							.collect(Collectors.toMap(SignSingleEntity::getName, Function.identity(),
									(existing, replacement) -> existing))
							.values());
						for (SignSingleEntity singleEntity : singleEntities) {
							Cell playerTitleCell = titleRow.createCell(playerCode + 4);
							playerTitleCell.setCellValue("选手" + playerCode);
							playerTitleCell.setCellStyle(titleCellStyle);
							sheet.autoSizeColumn(0);

							Cell playerCell = itemRow.createCell(playerCode + 4);
							playerCell.setCellValue(singleEntity.getName());
							playerCell.setCellStyle(itemCellStyle);
							sheet.autoSizeColumn(playerCode + 4);

							playerCode++;
						}

					}

					i++;

				}
			}

			// 保存文件
			String filePath = "./file.xlsx";

			try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
				workbook.write(fileOut);
			}
			String fileName = "秩序表-项目人员统计-" + gameId + ".xlsx";
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
	 * 导出人数统计表
	 * @param gameId 比赛编号
	 * @return
	 */
	private ApiResponse<?> exportByPlayerCounting(Long gameId) {
		try {
			// 创建工作簿
			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet(SHEET_NAME);

			Row titleRow1 = sheet.createRow(0);
			Font titleFont = workbook.createFont();
			titleFont.setFontName("Arial");
			titleFont.setFontHeightInPoints((short) 14);

			CellStyle titleCellStyle = workbook.createCellStyle();
			titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
			titleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			titleCellStyle.setWrapText(true); // 设置自动换行
			titleCellStyle.setFont(titleFont);

			Cell itemCell1 = titleRow1.createCell(0);
			itemCell1.setCellValue("序号");
			itemCell1.setCellStyle(titleCellStyle);
			sheet.autoSizeColumn(0);

			Cell itemCell2 = titleRow1.createCell(1);
			itemCell2.setCellValue("单位");
			itemCell2.setCellStyle(titleCellStyle);
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);

			Row titleRow2 = sheet.createRow(1);

			// 获取所有组别
			addMergedRegion(sheet, 0, 1, 0, 0);
			addMergedRegion(sheet, 0, 1, 1, 1);

			// 填充group信息
			List<SignSingleEntity> groupEntities = signApplyManager.getSignGroups(gameId);
			int groupCount = 2;
			for (SignSingleEntity signSingle : groupEntities) {
				GameGroupEntity groupEntity = signApplyManager.getGroupById(signSingle.getGroupId());

				Cell groupCell1 = titleRow1.createCell(groupCount);
				groupCell1.setCellValue(groupEntity.getGroupName());
				groupCell1.setCellStyle(titleCellStyle);
				sheet.autoSizeColumn(groupCount);

				Cell maleCell = titleRow2.createCell(groupCount);
				maleCell.setCellValue("男");
				maleCell.setCellStyle(titleCellStyle);

				groupCountKeyMap.put(signSingle.getGroupId() + "-1", groupCount);

				Cell femaleCell = titleRow2.createCell(groupCount + 1);
				femaleCell.setCellValue("女");
				femaleCell.setCellStyle(titleCellStyle);
				groupCountKeyMap.put(signSingle.getGroupId() + "-0", groupCount + 1);

				groupCount = groupCount + 2;
				addMergedRegion(sheet, 0, 0, groupCount - 2, groupCount - 1);

			}

			Cell itemTotalCell = titleRow1.createCell(groupCount);
			itemTotalCell.setCellValue("运动员小计");
			itemTotalCell.setCellStyle(titleCellStyle);
			sheet.autoSizeColumn(groupCount);

			addMergedRegion(sheet, 0, 1, groupCount, groupCount);

			int i = 2;
			List<SignSingleEntity> itemEntities = signApplyManager.getSignOrgs(gameId);

			if (CollUtil.isNotEmpty(itemEntities)) {
				int orgCode = 1;

				for (SignSingleEntity signSingleEntity : itemEntities) {

					Row itemRow = sheet.createRow(i);
					itemRow.setHeightInPoints(24);

					Font groupFont = workbook.createFont();
					groupFont.setFontName("Arial");
					groupFont.setFontHeightInPoints((short) 12);

					CellStyle itemCellStyle = workbook.createCellStyle();
					itemCellStyle.setAlignment(HorizontalAlignment.CENTER);
					itemCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
					itemCellStyle.setWrapText(true); // 设置自动换行
					itemCellStyle.setFont(groupFont);

					// 序号
					Cell srotNumCell = itemRow.createCell(0);
					srotNumCell.setCellValue(orgCode);
					srotNumCell.setCellStyle(itemCellStyle);
					orgCode++;

					// 单位
					Cell orgNumsCell = itemRow.createCell(1);
					orgNumsCell.setCellValue(signSingleEntity.getOrgName());
					orgNumsCell.setCellStyle(itemCellStyle);
					sheet.autoSizeColumn(1);

					for (int j = 2; j <= groupCount; j++) {
						Cell countCell = itemRow.createCell(j);
						countCell.setCellValue(0);
						countCell.setCellStyle(itemCellStyle);
					}

					Long orgId = signSingleEntity.getOrgId();
					// 获取报名用户信息
					List<SignSingleEntity> orgEntities = signApplyManager.countPlayerCountingByOrgId(orgId);

					Map<String, Long> sumByGroupAndSex = orgEntities.stream()
						.collect(Collectors.groupingBy(x -> x.getGroupId() + "-" + x.getSex(), Collectors.counting()));
					log.info("sumByGroupAndSex-value:{}", JSON.toJSONString(sumByGroupAndSex));
					log.info("groupCountKeyMap-value:{}", JSON.toJSONString(groupCountKeyMap));

					if (CollUtil.isNotEmpty(orgEntities)) {

						for (SignSingleEntity singleEntity : orgEntities) {
							String key = singleEntity.getGroupId() + "-" + singleEntity.getSex();
							if (groupCountKeyMap.containsKey(key)) {
								log.info("export-key:{}-value:{}", key, groupCountKeyMap.get(key));
								Cell playerTitleCell = itemRow.createCell(groupCountKeyMap.get(key));
								playerTitleCell.setCellValue(sumByGroupAndSex.getOrDefault(key, 0L));
								playerTitleCell.setCellStyle(titleCellStyle);
							}

							Cell totalCell = itemRow.createCell(groupCount);
							totalCell.setCellValue(sumByGroupAndSex.values().stream().mapToLong(Long::longValue).sum());
							totalCell.setCellStyle(titleCellStyle);
						}

					}

					i++;

				}
			}

			// 保存文件
			String filePath = "./file.xlsx";

			try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
				workbook.write(fileOut);
			}
			String fileName = "秩序表-人数信息统计-" + gameId + ".xlsx";
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
	 * 导出人次统计表
	 * @param gameId 比赛编号
	 * @return
	 */
	private ApiResponse<?> exportByPlayerTimes(Long gameId) {
		try {
			// 创建工作簿
			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet(SHEET_NAME);

			Row titleRow1 = sheet.createRow(0);
			Font titleFont = workbook.createFont();
			titleFont.setFontName("Arial");
			titleFont.setFontHeightInPoints((short) 14);

			CellStyle titleCellStyle = workbook.createCellStyle();
			titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
			titleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			titleCellStyle.setWrapText(true); // 设置自动换行
			titleCellStyle.setFont(titleFont);

			Cell itemCell1 = titleRow1.createCell(0);
			itemCell1.setCellValue("序号");
			itemCell1.setCellStyle(titleCellStyle);
			sheet.autoSizeColumn(0);

			Cell itemCell2 = titleRow1.createCell(1);
			itemCell2.setCellValue("单位");
			itemCell2.setCellStyle(titleCellStyle);
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);

			Row titleRow2 = sheet.createRow(1);

			// 获取所有组别
			addMergedRegion(sheet, 0, 1, 0, 0);
			addMergedRegion(sheet, 0, 1, 1, 1);

			// 填充group信息
			List<SignSingleEntity> groupEntities = signApplyManager.getSignGroups(gameId);
			int groupCount = 2;
			for (SignSingleEntity signSingle : groupEntities) {
				GameGroupEntity groupEntity = signApplyManager.getGroupById(signSingle.getGroupId());

				Cell groupCell1 = titleRow1.createCell(groupCount);
				groupCell1.setCellValue(groupEntity.getGroupName());
				groupCell1.setCellStyle(titleCellStyle);
				sheet.autoSizeColumn(groupCount);

				Cell maleCell = titleRow2.createCell(groupCount);
				maleCell.setCellValue("男");
				maleCell.setCellStyle(titleCellStyle);

				groupCountKeyMap.put(signSingle.getGroupId() + "-1", groupCount);

				Cell femaleCell = titleRow2.createCell(groupCount + 1);
				femaleCell.setCellValue("女");
				femaleCell.setCellStyle(titleCellStyle);
				groupCountKeyMap.put(signSingle.getGroupId() + "-0", groupCount + 1);

				groupCount = groupCount + 2;
				addMergedRegion(sheet, 0, 0, groupCount - 2, groupCount - 1);

			}

			Cell itemTotalCell = titleRow1.createCell(groupCount);
			itemTotalCell.setCellValue("运动员小计");
			itemTotalCell.setCellStyle(titleCellStyle);
			sheet.autoSizeColumn(groupCount);

			addMergedRegion(sheet, 0, 1, groupCount, groupCount);

			int i = 2;
			List<SignSingleEntity> itemEntities = signApplyManager.getSignOrgs(gameId);

			if (CollUtil.isNotEmpty(itemEntities)) {
				int orgCode = 1;

				for (SignSingleEntity signSingleEntity : itemEntities) {

					Row itemRow = sheet.createRow(i);
					itemRow.setHeightInPoints(24);

					Font groupFont = workbook.createFont();
					groupFont.setFontName("Arial");
					groupFont.setFontHeightInPoints((short) 12);

					CellStyle itemCellStyle = workbook.createCellStyle();
					itemCellStyle.setAlignment(HorizontalAlignment.CENTER);
					itemCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
					itemCellStyle.setWrapText(true); // 设置自动换行
					itemCellStyle.setFont(groupFont);

					// 序号
					Cell srotNumCell = itemRow.createCell(0);
					srotNumCell.setCellValue(orgCode);
					srotNumCell.setCellStyle(itemCellStyle);
					orgCode++;

					// 单位
					Cell orgNumsCell = itemRow.createCell(1);
					orgNumsCell.setCellValue(signSingleEntity.getOrgName());
					orgNumsCell.setCellStyle(itemCellStyle);
					sheet.autoSizeColumn(1);

					for (int j = 2; j <= groupCount; j++) {
						Cell countCell = itemRow.createCell(j);
						countCell.setCellValue(0);
						countCell.setCellStyle(itemCellStyle);
					}

					Long orgId = signSingleEntity.getOrgId();
					// 获取报名用户信息
					List<SignSingleEntity> orgEntities = signApplyManager.countPlayerTimesByOrgId(orgId);

					Map<String, Long> sumByGroupAndSex = orgEntities.stream()
						.collect(Collectors.groupingBy(x -> x.getGroupId() + "-" + x.getSex(), Collectors.counting()));

					if (CollUtil.isNotEmpty(orgEntities)) {

						for (SignSingleEntity singleEntity : orgEntities) {
							String key = singleEntity.getGroupId() + "-" + singleEntity.getSex();
							if (groupCountKeyMap.containsKey(key)) {

								Cell playerTitleCell = itemRow.createCell(groupCountKeyMap.get(key));
								playerTitleCell.setCellValue(sumByGroupAndSex.getOrDefault(key, 0L));
								playerTitleCell.setCellStyle(titleCellStyle);
							}

							Cell totalCell = itemRow.createCell(groupCount);
							totalCell.setCellValue(sumByGroupAndSex.values().stream().mapToLong(Long::longValue).sum());
							totalCell.setCellStyle(titleCellStyle);
						}

					}

					i++;

				}
			}

			// 保存文件
			String filePath = "./file.xlsx";

			try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
				workbook.write(fileOut);
			}
			String fileName = "秩序表-人次信息统计-" + gameId + ".xlsx";
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
	 * 导出人数统计表
	 * @param gameId 比赛编号
	 * @return
	 */
	private ApiResponse<?> exportByProject(Long gameId) {
		try {
			// 创建工作簿
			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet(SHEET_NAME);

			// 创建行
			Row row = sheet.createRow(0);

			// 设置字体样式
			Font font = workbook.createFont();
			font.setFontName("Arial");
			font.setFontHeightInPoints((short) 18);

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
			cell.setCellValue("项目统计表");
			cell.setCellStyle(cellStyle);

			// 设置单元格宽度
			// sheet.setColumnWidth(0, 20 * 256); // 设置列宽度

			// 设置整个表格的宽度适配 A4 纸张的宽度
			sheet.setFitToPage(true);
			PrintSetup printSetup = sheet.getPrintSetup();
			printSetup.setPaperSize(PrintSetup.A4_PAPERSIZE);
			sheet.setZoom((short) 100); // 将 Fit Width 设置为 1

			// 新增空白行
			addBlankRow(sheet, 1, 5);
			Row titleRow = sheet.createRow(2);
			Font titleFont = workbook.createFont();
			titleFont.setFontName("Arial");
			titleFont.setFontHeightInPoints((short) 14);

			CellStyle titleCellStyle = workbook.createCellStyle();
			titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
			titleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			titleCellStyle.setWrapText(true); // 设置自动换行
			titleCellStyle.setFont(titleFont);

			Cell itemCell1 = titleRow.createCell(0);
			itemCell1.setCellValue("序号");
			itemCell1.setCellStyle(titleCellStyle);
			sheet.autoSizeColumn(0);

			Cell itemCell2 = titleRow.createCell(1);
			itemCell2.setCellValue("组别名称");
			itemCell2.setCellStyle(titleCellStyle);
			sheet.autoSizeColumn(1);

			Cell itemCell3 = titleRow.createCell(2);
			itemCell3.setCellValue("项目名称");
			itemCell3.setCellStyle(titleCellStyle);
			sheet.autoSizeColumn(2);

			Cell itemCell4 = titleRow.createCell(3);
			itemCell4.setCellValue("单位数");
			itemCell4.setCellStyle(titleCellStyle);
			sheet.autoSizeColumn(3);

			Cell itemCell5 = titleRow.createCell(4);
			itemCell5.setCellValue("队伍数");
			itemCell5.setCellStyle(titleCellStyle);
			sheet.autoSizeColumn(4);

			Cell itemCell6 = titleRow.createCell(5);
			itemCell6.setCellValue("人次");
			itemCell6.setCellStyle(titleCellStyle);
			sheet.autoSizeColumn(5);

			int i = 3;
			List<SignSingleEntity> itemEntities = signApplyManager.getSignItems(gameId);
			if (CollUtil.isNotEmpty(itemEntities)) {
				int orgCode = 1;
				//
				for (SignSingleEntity signSingleEntity : itemEntities) {
					// // 填充group信息
					GameGroupEntity groupEntity = signApplyManager.getGroupById(signSingleEntity.getGroupId());
					GameItemEntity itemEntity = signApplyManager.getItemById(signSingleEntity.getItemId());

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

					Cell srotNumCell = itemRow.createCell(0);
					srotNumCell.setCellValue(orgCode);
					srotNumCell.setCellStyle(itemCellStyle);

					Cell groupCell = itemRow.createCell(1);
					groupCell.setCellValue(groupEntity.getGroupName());
					groupCell.setCellStyle(itemCellStyle);

					Cell itemCell = itemRow.createCell(2);
					itemCell.setCellValue(itemEntity.getItemName());
					itemCell.setCellStyle(itemCellStyle);

					Long itemId = signSingleEntity.getItemId();
					// 统计单位数
					long orgNums = signApplyManager.countOrgByItemId(itemId);
					Cell orgNumsCell = itemRow.createCell(3);
					orgNumsCell.setCellValue(orgNums);
					orgNumsCell.setCellStyle(itemCellStyle);
					// 统计队伍数
					long teamNums = signApplyManager.countTeamByItemId(itemId);
					Cell teamNumsCell = itemRow.createCell(4);
					teamNumsCell.setCellValue(teamNums);
					teamNumsCell.setCellStyle(itemCellStyle);
					// 统计人次数
					long playerTimes = signApplyManager.countPlayerTimesByItemId(itemId);
					Cell playerTimesNumsCell = itemRow.createCell(5);
					playerTimesNumsCell.setCellValue(playerTimes);
					playerTimesNumsCell.setCellStyle(itemCellStyle);
					i++;
				}
			}

			// 合并单元格
			addMergedRegion(sheet, 0, 0, 0, 5);

			// 保存文件
			String filePath = "./file.xlsx";

			try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
				workbook.write(fileOut);
			}
			String fileName = "秩序表-项目（队伍）-" + gameId + ".xlsx";
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
	 * 根据项目导出(队伍)
	 * @param gameId 比赛编号
	 * @return
	 */
	private ApiResponse<?> exportByItemAndTeam(Long gameId) {
		try {
			// 创建工作簿
			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet(SHEET_NAME);

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
			cell.setCellValue("秩序册-项目");
			cell.setCellStyle(cellStyle);

			// 设置单元格宽度
			sheet.setColumnWidth(0, 12 * 256); // 设置列宽度

			// 设置整个表格的宽度适配 A4 纸张的宽度
			sheet.setFitToPage(true);
			PrintSetup printSetup = sheet.getPrintSetup();
			printSetup.setFitWidth((short) 1); // 将 Fit Width 设置为 1

			// 新增空白行
			addBlankRow(sheet, 1);

			int i = 2;
			List<SignSingleEntity> itemEntities = signApplyManager.getSignItems(gameId);
			if (CollUtil.isNotEmpty(itemEntities)) {
				int orgCode = 1;

				for (SignSingleEntity signSingleEntity : itemEntities) {
					// 填充group信息
					GameGroupEntity groupEntity = signApplyManager.getGroupById(signSingleEntity.getGroupId());
					GameItemEntity itemEntity = signApplyManager.getItemById(signSingleEntity.getItemId());

					Row itemRow = sheet.createRow(i);
					itemRow.setHeightInPoints(24);

					Font groupFont = workbook.createFont();
					groupFont.setFontName("Arial");
					groupFont.setFontHeightInPoints((short) 15);

					itemRow.setHeightInPoints(24);

					CellStyle itemCellStyle = workbook.createCellStyle();
					itemCellStyle.setAlignment(HorizontalAlignment.CENTER);
					itemCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
					itemCellStyle.setWrapText(true); // 设置自动换行
					itemCellStyle.setFont(groupFont);

					Cell itemCell = itemRow.createCell(0);
					itemCell.setCellValue(groupEntity.getGroupName() + " - " + itemEntity.getItemName());
					itemCell.setCellStyle(itemCellStyle);

					addMergedRegion(sheet, i, i, 0, 9);

					i++;

					Long groupId = signSingleEntity.getGroupId();
					Long itemId = signSingleEntity.getItemId();
					// 填充报名信息 查询该item下，所有队伍信息
					List<SignSingleEntity> teamEnties = signApplyManager.getTeamByGroupIdAndItemId(gameId, groupId,
							itemId);
					if (CollUtil.isNotEmpty(teamEnties)) {
						teamEnties = teamEnties.stream()
							.sorted(Comparator.comparing(SignSingleEntity::getTeamId).reversed())
							.collect(Collectors.toList());

						for (SignSingleEntity teamEntity : teamEnties) {
							List<SignSingleEntity> teamSignEnties = signApplyManager.getApplyByItemIdAndTeamId(gameId,
									groupId, itemId, teamEntity.getTeamId());

							if (teamEntity.getTeamId() > 0) {
								SignTeamEntity signTeamEntity = signApplyManager.getTeamById(teamEntity.getTeamId());

								addBlankRow(sheet, i);
								i++;
								Row orgRow = sheet.createRow(i);
								orgRow.setHeightInPoints(20);

								Font orgFont = workbook.createFont();
								orgFont.setFontName("Arial");
								orgFont.setFontHeightInPoints((short) 11);

								CellStyle orgCellStyle = workbook.createCellStyle();
								orgCellStyle.setAlignment(HorizontalAlignment.CENTER);
								orgCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
								orgCellStyle.setFont(orgFont);
								orgCellStyle.setWrapText(true); // 设置自动换行

								// 编号
								Cell orgCellCode = orgRow.createCell(0);

								String orgCodeStr = String.format("%03d", orgCode);
								orgCellCode.setCellValue(orgCodeStr);
								orgCellCode.setCellStyle(orgCellStyle);

								// 队伍名
								Cell orgCellName = orgRow.createCell(1);
								orgCellName.setCellValue(signTeamEntity.getTeamName());
								orgCellName.setCellStyle(orgCellStyle);
								addMergedRegion(sheet, i, i, 1, 8);

								orgCode++;
								i++;

								List<Long> teamIds = teamSignEnties.stream()
									.map(SignSingleEntity::getTeamId)
									.distinct()
									.collect(Collectors.toList());
								// 有队伍参赛的情况，生成领队和教练信息
								if (CollUtil.isNotEmpty(teamIds)) {
									List<SignTeamEntity> teamEntities = signApplyManager.getTeamsByIds(gameId, teamIds);

									// 领队
									List<String> leaderEntity = teamEntities.stream()
										.map(SignTeamEntity::getLeaderName)
										.collect(Collectors.toList());
									String leaderEntityStr = String.join(",", leaderEntity);
									addTeamRow(workbook, sheet, i, "领队", leaderEntityStr);
									i++;
									// 教练
									List<String> coachEntity = teamEntities.stream()
										.map(SignTeamEntity::getCoachName)
										.collect(Collectors.toList());
									String coachEntityStr = String.join(",", coachEntity);
									addTeamRow(workbook, sheet, i, "教练", coachEntityStr);
									i++;
								}

								// 男运动员
								List<SignSingleEntity> maleEntities = teamSignEnties.stream()
									.filter(item -> item.getSex() == 1)
									.collect(Collectors.toList());
								if (CollUtil.isNotEmpty(maleEntities)) {
									addPlayerTitleRow(workbook, sheet, i, "男运动员");
									i++;
									int maleCellIndex = 0;
									for (SignSingleEntity signSingle : maleEntities) {
										addPlayerContentRow(workbook, sheet, i, maleCellIndex, signSingle.getName());
										maleCellIndex++;
										if (maleCellIndex >= 10) {
											i++;
											maleCellIndex = 0;
										}
									}
									i++;
								}
								// 女运动员
								List<SignSingleEntity> femaleEntities = teamSignEnties.stream()
									.filter(item -> item.getSex() == 0)
									.collect(Collectors.toList());
								if (CollUtil.isNotEmpty(femaleEntities)) {
									addPlayerTitleRow(workbook, sheet, i, "女运动员");
									i++;
									int femaleCellIndex = 0;
									for (SignSingleEntity signSingle : femaleEntities) {
										addPlayerContentRow(workbook, sheet, i, femaleCellIndex, signSingle.getName());
										femaleCellIndex++;
										if (femaleCellIndex >= 10) {
											i++;
											femaleCellIndex = 0;
										}
									}
									i++;
								}
							}
							else {
								addBlankRow(sheet, i);
								i++;
								Row orgRow = sheet.createRow(i);
								orgRow.setHeightInPoints(20);

								Font orgFont = workbook.createFont();
								orgFont.setFontName("Arial");
								orgFont.setFontHeightInPoints((short) 11);

								CellStyle orgCellStyle = workbook.createCellStyle();
								orgCellStyle.setAlignment(HorizontalAlignment.CENTER);
								orgCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
								orgCellStyle.setFont(orgFont);
								orgCellStyle.setWrapText(true); // 设置自动换行

								// 编号
								Cell orgCellCode = orgRow.createCell(0);

								String orgCodeStr = String.format("%03d", orgCode);
								orgCellCode.setCellValue(orgCodeStr);
								orgCellCode.setCellStyle(orgCellStyle);

								// 队伍名
								Cell orgCellName = orgRow.createCell(1);
								orgCellName.setCellValue("个人选手");
								orgCellName.setCellStyle(orgCellStyle);
								addMergedRegion(sheet, i, i, 1, 8);

								orgCode++;
								i++;

								// 男运动员
								List<SignSingleEntity> maleEntities = teamSignEnties.stream()
									.filter(item -> item.getSex() == 1)
									.collect(Collectors.toList());
								if (CollUtil.isNotEmpty(maleEntities)) {
									addPlayerTitleRow(workbook, sheet, i, "男运动员");
									i++;
									int maleCellIndex = 0;
									for (SignSingleEntity signSingle : maleEntities) {
										addPlayerContentRow(workbook, sheet, i, maleCellIndex, signSingle.getName());
										maleCellIndex++;
										if (maleCellIndex >= 10) {
											i++;
											maleCellIndex = 0;
										}
									}
									i++;
								}
								// 女运动员
								List<SignSingleEntity> femaleEntities = teamSignEnties.stream()
									.filter(item -> item.getSex() == 0)
									.collect(Collectors.toList());
								if (CollUtil.isNotEmpty(femaleEntities)) {
									addPlayerTitleRow(workbook, sheet, i, "女运动员");
									i++;
									int femaleCellIndex = 0;
									for (SignSingleEntity signSingle : femaleEntities) {
										addPlayerContentRow(workbook, sheet, i, femaleCellIndex, signSingle.getName());
										femaleCellIndex++;
										if (femaleCellIndex >= 10) {
											i++;
											femaleCellIndex = 0;
										}
									}
									i++;
								}
								addBlankRow(sheet, i);
								i++;
							}

						}
						addBlankRow(sheet, i);
						i++;

					}

				}
			}

			// 合并单元格
			addMergedRegion(sheet, 0, 0, 0, 9);

			// 保存文件
			String filePath = "./file.xlsx";

			try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
				workbook.write(fileOut);
			}
			String fileName = "秩序表-项目（队伍）-" + gameId + ".xlsx";
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
	 * 根据项目导出
	 * @param gameId 比赛编号
	 * @return
	 */
	private ApiResponse<?> exportByItemAndOrg(Long gameId) {
		try {
			// 创建工作簿
			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet(SHEET_NAME);

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
			cell.setCellValue("秩序册-项目");
			cell.setCellStyle(cellStyle);

			// 设置单元格宽度
			sheet.setColumnWidth(0, 12 * 256); // 设置列宽度

			// 设置整个表格的宽度适配 A4 纸张的宽度
			sheet.setFitToPage(true);
			PrintSetup printSetup = sheet.getPrintSetup();
			printSetup.setFitWidth((short) 1); // 将 Fit Width 设置为 1

			// 新增空白行
			addBlankRow(sheet, 1);

			int i = 2;
			List<SignSingleEntity> itemEntities = signApplyManager.getSignItems(gameId);
			if (CollUtil.isNotEmpty(itemEntities)) {
				int orgCode = 1;

				for (SignSingleEntity signSingleEntity : itemEntities) {
					// 填充group信息
					GameGroupEntity groupEntity = signApplyManager.getGroupById(signSingleEntity.getGroupId());
					GameItemEntity itemEntity = signApplyManager.getItemById(signSingleEntity.getItemId());

					Row itemRow = sheet.createRow(i);
					itemRow.setHeightInPoints(24);

					Font groupFont = workbook.createFont();
					groupFont.setFontName("Arial");
					groupFont.setFontHeightInPoints((short) 15);

					itemRow.setHeightInPoints(24);

					CellStyle itemCellStyle = workbook.createCellStyle();
					itemCellStyle.setAlignment(HorizontalAlignment.CENTER);
					itemCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
					itemCellStyle.setWrapText(true); // 设置自动换行
					itemCellStyle.setFont(groupFont);

					Cell itemCell = itemRow.createCell(0);
					itemCell.setCellValue(groupEntity.getGroupName() + " - " + itemEntity.getItemName());
					itemCell.setCellStyle(itemCellStyle);

					addMergedRegion(sheet, i, i, 0, 9);

					i++;

					Long groupId = signSingleEntity.getGroupId();
					Long itemId = signSingleEntity.getItemId();
					// 填充报名信息 查询该group下，所有单位信息
					List<SignSingleEntity> orgEnties = signApplyManager.getOrgsByGroupIdAndItemId(gameId, groupId,
							itemId);
					if (CollUtil.isNotEmpty(orgEnties)) {
						for (SignSingleEntity orgEntity : orgEnties) {
							addBlankRow(sheet, i);
							i++;
							Row orgRow = sheet.createRow(i);
							orgRow.setHeightInPoints(20);

							Font orgFont = workbook.createFont();
							orgFont.setFontName("Arial");
							orgFont.setFontHeightInPoints((short) 11);

							CellStyle orgCellStyle = workbook.createCellStyle();
							orgCellStyle.setAlignment(HorizontalAlignment.CENTER);
							orgCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
							orgCellStyle.setFont(orgFont);
							orgCellStyle.setWrapText(true); // 设置自动换行

							// 编号
							Cell orgCellCode = orgRow.createCell(0);

							String orgCodeStr = String.format("%03d", orgCode);
							orgCellCode.setCellValue(orgCodeStr);
							orgCellCode.setCellStyle(orgCellStyle);

							// 队伍名
							Cell orgCellName = orgRow.createCell(1);
							orgCellName.setCellValue(orgEntity.getOrgName());
							orgCellName.setCellStyle(orgCellStyle);
							addMergedRegion(sheet, i, i, 1, 8);

							orgCode++;
							i++;

							String orgName = orgEntity.getOrgName();
							// 查询每个组织下具体的报名信息
							List<SignSingleEntity> singleEntities = signApplyManager.getApplyByOrgAndGroupId(gameId,
									groupId, orgName);

							if (CollUtil.isNotEmpty(singleEntities)) {
								List<Long> teamIds = singleEntities.stream()
									.map(SignSingleEntity::getTeamId)
									.distinct()
									.collect(Collectors.toList());
								// 有队伍参赛的情况，生成领队和教练信息
								if (CollUtil.isNotEmpty(teamIds)) {
									List<SignTeamEntity> teamEntities = signApplyManager.getTeamsByIds(gameId, teamIds);

									// 领队
									List<String> leaderEntity = teamEntities.stream()
										.map(SignTeamEntity::getLeaderName)
										.collect(Collectors.toList());
									String leaderEntityStr = String.join(",", leaderEntity);
									addTeamRow(workbook, sheet, i, "领队", leaderEntityStr);
									i++;
									// 教练
									List<String> coachEntity = teamEntities.stream()
										.map(SignTeamEntity::getCoachName)
										.collect(Collectors.toList());
									String coachEntityStr = String.join(",", coachEntity);
									addTeamRow(workbook, sheet, i, "教练", coachEntityStr);
									i++;
								}

								// 男运动员
								List<SignSingleEntity> maleEntities = singleEntities.stream()
									.filter(item -> item.getSex() == 1)
									.collect(Collectors.toList());
								if (CollUtil.isNotEmpty(maleEntities)) {
									addPlayerTitleRow(workbook, sheet, i, "男运动员");
									i++;
									int maleCellIndex = 0;
									for (SignSingleEntity signSingle : maleEntities) {
										addPlayerContentRow(workbook, sheet, i, maleCellIndex, signSingle.getName());
										maleCellIndex++;
										if (maleCellIndex >= 10) {
											i++;
											maleCellIndex = 0;
										}
									}
									i++;
								}
								// 女运动员
								List<SignSingleEntity> femaleEntities = singleEntities.stream()
									.filter(item -> item.getSex() == 0)
									.collect(Collectors.toList());
								if (CollUtil.isNotEmpty(femaleEntities)) {
									addPlayerTitleRow(workbook, sheet, i, "女运动员");
									i++;
									int femaleCellIndex = 0;
									for (SignSingleEntity signSingle : femaleEntities) {
										addPlayerContentRow(workbook, sheet, i, femaleCellIndex, signSingle.getName());
										femaleCellIndex++;
										if (femaleCellIndex >= 10) {
											i++;
											femaleCellIndex = 0;
										}
									}
									i++;
								}
							}

						}
						addBlankRow(sheet, i);
						i++;

					}

				}
			}

			// 合并单元格
			addMergedRegion(sheet, 0, 0, 0, 9);

			// 保存文件
			String filePath = "./file.xlsx";

			try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
				workbook.write(fileOut);
			}
			String fileName = "秩序表-项目-" + gameId + ".xlsx";
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
	 * 根据组织导出
	 * @param gameId 比赛编号
	 * @return
	 */
	private ApiResponse<?> exportByOrg(Long gameId) {
		try {
			// 创建工作簿
			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet(SHEET_NAME);

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
			cell.setCellValue("秩序册-名单");
			cell.setCellStyle(cellStyle);

			// 设置单元格宽度
			sheet.setColumnWidth(0, 12 * 256); // 设置列宽度

			// 设置整个表格的宽度适配 A4 纸张的宽度
			sheet.setFitToPage(true);
			PrintSetup printSetup = sheet.getPrintSetup();
			printSetup.setFitWidth((short) 1); // 将 Fit Width 设置为 1
			printSetup.setPaperSize(PrintSetup.A4_PAPERSIZE); // 设置纸张大小
			printSetup.setFitWidth((short) 1); // 设置页面适应宽度
			printSetup.setFitHeight((short) 0); // 设置页面适应高度
			printSetup.setHeaderMargin(0.7); // 设置页眉边距（单位：英寸）
			printSetup.setFooterMargin(0.7); // 设置页脚边距（单位：英寸）

			printSetup.setScale((short) 100); // 设置缩放比例为100%
			sheet.setZoom(100);
			// 新增空白行
			addBlankRow(sheet, 1);

			int i = 2;
			List<SignSingleEntity> orgEntities = signApplyManager.getSignOrgs(gameId);
			if (CollUtil.isNotEmpty(orgEntities)) {
				int orgCode = 1;
				for (SignSingleEntity orgEntity : orgEntities) {
					addBlankRow(sheet, i);
					i++;
					Row orgRow = sheet.createRow(i);
					orgRow.setHeightInPoints(20);

					Font orgFont = workbook.createFont();
					orgFont.setFontName("Arial");
					orgFont.setFontHeightInPoints((short) 11);

					CellStyle orgCellStyle = workbook.createCellStyle();
					orgCellStyle.setAlignment(HorizontalAlignment.CENTER);
					orgCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
					orgCellStyle.setFont(orgFont);
					orgCellStyle.setWrapText(true); // 设置自动换行

					// 编号
					Cell orgCellCode = orgRow.createCell(0);

					String orgCodeStr = String.format("%03d", orgCode);
					orgCellCode.setCellValue(orgCodeStr);
					orgCellCode.setCellStyle(orgCellStyle);

					// 队伍名
					Cell orgCellName = orgRow.createCell(1);
					orgCellName.setCellValue(orgEntity.getOrgName());
					orgCellStyle.setWrapText(true); // 设置自动换行
					orgCellName.setCellStyle(orgCellStyle);
					addMergedRegion(sheet, i, i, 1, 8);

					orgCode++;
					i++;

					String orgName = orgEntity.getOrgName();
					// 查询每个组织下具体的报名信息
					List<SignSingleEntity> singleEntities = signApplyManager.getApplyByOrg(gameId, orgName);

					if (CollUtil.isNotEmpty(singleEntities)) {
						List<Long> teamIds = singleEntities.stream()
							.map(SignSingleEntity::getTeamId)
							.distinct()
							.collect(Collectors.toList());
						// 有队伍参赛的情况，生成领队和教练信息
						if (CollUtil.isNotEmpty(teamIds)) {
							List<SignTeamEntity> teamEntities = signApplyManager.getTeamsByIds(gameId, teamIds);

							// 领队
							List<String> leaderEntity = teamEntities.stream()
								.map(SignTeamEntity::getLeaderName)
								.distinct()
								.collect(Collectors.toList());
							String leaderEntityStr = String.join(",", leaderEntity);
							addTeamRow(workbook, sheet, i, "领队", leaderEntityStr);
							i++;
							// 教练
							List<String> coachEntity = teamEntities.stream()
								.map(SignTeamEntity::getCoachName)
								.distinct()
								.collect(Collectors.toList());
							String coachEntityStr = String.join(",", coachEntity);
							addTeamRow(workbook, sheet, i, "教练", coachEntityStr);
							i++;
						}

						// 男运动员
						List<SignSingleEntity> maleEntities = singleEntities.stream()
							.filter(item -> item.getSex() == 1)
							.collect(Collectors.collectingAndThen(
									Collectors.toCollection(
											() -> new TreeSet<>(Comparator.comparing(SignSingleEntity::getName))),
									ArrayList::new));

						if (CollUtil.isNotEmpty(maleEntities)) {
							addPlayerTitleRow(workbook, sheet, i, "男运动员");
							i++;
							int maleCellIndex = 0;
							for (SignSingleEntity signSingle : maleEntities) {
								addPlayerContentRow(workbook, sheet, i, maleCellIndex, signSingle.getName());
								maleCellIndex++;
								if (maleCellIndex >= 10) {
									i++;
									maleCellIndex = 0;
								}
							}
							i++;
						}
						// 女运动员
						List<SignSingleEntity> femaleEntities = singleEntities.stream()
							.filter(item -> item.getSex() == 0)
							.collect(Collectors.collectingAndThen(
									Collectors.toCollection(
											() -> new TreeSet<>(Comparator.comparing(SignSingleEntity::getName))),
									ArrayList::new));

						if (CollUtil.isNotEmpty(femaleEntities)) {
							addPlayerTitleRow(workbook, sheet, i, "女运动员");
							i++;
							int femaleCellIndex = 0;
							for (SignSingleEntity signSingle : femaleEntities) {
								addPlayerContentRow(workbook, sheet, i, femaleCellIndex, signSingle.getName());
								femaleCellIndex++;
								if (femaleCellIndex >= 10) {
									i++;
									femaleCellIndex = 0;
								}
							}
							i++;
						}
					}

				}
			}

			// 合并单元格
			addMergedRegion(sheet, 0, 0, 0, 9);
			sheet.autoSizeColumn(0);

			// 保存文件
			String filePath = "./file.xlsx";

			try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
				workbook.write(fileOut);
			}
			String fileName = "秩序表-名单-" + gameId + ".xlsx";
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
	 * 根据队伍导出
	 * @param gameId 比赛编号
	 * @return ApiResponse
	 */
	private ApiResponse<?> exportByTeam(Long gameId) {
		try {
			// 创建工作簿
			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet(SHEET_NAME);

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
			cell.setCellValue("秩序册-名单（队伍）");
			cell.setCellStyle(cellStyle);

			// 设置单元格宽度
			sheet.setColumnWidth(0, 12 * 256); // 设置列宽度

			// 设置整个表格的宽度适配 A4 纸张的宽度
			sheet.setFitToPage(true);
			PrintSetup printSetup = sheet.getPrintSetup();
			printSetup.setFitWidth((short) 1); // 将 Fit Width 设置为 1
			printSetup.setPaperSize(PrintSetup.A4_PAPERSIZE); // 设置纸张大小
			printSetup.setFitWidth((short) 1); // 设置页面适应宽度
			printSetup.setFitHeight((short) 0); // 设置页面适应高度
			printSetup.setHeaderMargin(0.7); // 设置页眉边距（单位：英寸）
			printSetup.setFooterMargin(0.7); // 设置页脚边距（单位：英寸）

			printSetup.setScale((short) 100); // 设置缩放比例为100%
			sheet.setZoom(100);
			// 新增空白行
			addBlankRow(sheet, 1);

			int i = 2;
			List<SignSingleEntity> teamEntities = signApplyManager.getSignTeams(gameId);
			if (CollUtil.isNotEmpty(teamEntities)) {
				int orgCode = 1;
				teamEntities = teamEntities.stream()
					.sorted(Comparator.comparing(SignSingleEntity::getTeamId).reversed())
					.collect(Collectors.toList());
				for (SignSingleEntity teamEntity : teamEntities) {
					SignTeamEntity signTeamEntity = null;
					if (teamEntity.getTeamId() > 0) {
						signTeamEntity = signApplyManager.getTeamById(teamEntity.getTeamId());
					}
					addBlankRow(sheet, i);
					i++;
					Row orgRow = sheet.createRow(i);
					orgRow.setHeightInPoints(20);

					Font orgFont = workbook.createFont();
					orgFont.setFontName("Arial");
					orgFont.setFontHeightInPoints((short) 11);

					CellStyle orgCellStyle = workbook.createCellStyle();
					orgCellStyle.setAlignment(HorizontalAlignment.CENTER);
					orgCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
					orgCellStyle.setFont(orgFont);
					orgCellStyle.setWrapText(true); // 设置自动换行

					// 编号
					Cell orgCellCode = orgRow.createCell(0);

					String orgCodeStr = String.format("%03d", orgCode);
					orgCellCode.setCellValue(orgCodeStr);
					orgCellCode.setCellStyle(orgCellStyle);

					// 队伍名
					Cell orgCellName = orgRow.createCell(1);
					orgCellName.setCellValue(Objects.nonNull(signTeamEntity) ? signTeamEntity.getTeamName() : "个人选手");
					orgCellStyle.setWrapText(true); // 设置自动换行
					orgCellName.setCellStyle(orgCellStyle);
					addMergedRegion(sheet, i, i, 1, 8);

					orgCode++;
					i++;

					Long teamId = teamEntity.getTeamId();
					// 查询每个组织下具体的报名信息
					List<SignSingleEntity> singleEntities = signApplyManager.getApplyByTeam(gameId, teamId);

					if (CollUtil.isNotEmpty(singleEntities)) {
						// 有队伍参赛的情况，生成领队和教练信息
						if (Objects.nonNull(signTeamEntity)) {
							// 领队
							addTeamRow(workbook, sheet, i, "领队", signTeamEntity.getLeaderName());
							i++;
							// 教练
							addTeamRow(workbook, sheet, i, "教练", signTeamEntity.getCoachName());
							i++;
						}

						// 男运动员
						List<SignSingleEntity> maleEntities = singleEntities.stream()
							.filter(item -> item.getSex() == 1)
							.collect(Collectors.toList());
						if (CollUtil.isNotEmpty(maleEntities)) {
							addPlayerTitleRow(workbook, sheet, i, "男运动员");
							i++;
							int maleCellIndex = 0;
							for (SignSingleEntity signSingle : maleEntities) {
								addPlayerContentRow(workbook, sheet, i, maleCellIndex, signSingle.getName());
								maleCellIndex++;
								if (maleCellIndex >= 10) {
									i++;
									maleCellIndex = 0;
								}
							}
							i++;
						}
						// 女运动员
						List<SignSingleEntity> femaleEntities = singleEntities.stream()
							.filter(item -> item.getSex() == 0)
							.collect(Collectors.toList());
						if (CollUtil.isNotEmpty(femaleEntities)) {
							addPlayerTitleRow(workbook, sheet, i, "女运动员");
							i++;
							int femaleCellIndex = 0;
							for (SignSingleEntity signSingle : femaleEntities) {
								addPlayerContentRow(workbook, sheet, i, femaleCellIndex, signSingle.getName());
								femaleCellIndex++;
								if (femaleCellIndex >= 10) {
									i++;
									femaleCellIndex = 0;
								}
							}
							i++;
						}
					}

				}
			}

			// 合并单元格
			addMergedRegion(sheet, 0, 0, 0, 9);
			sheet.autoSizeColumn(0);

			// 保存文件
			String filePath = "./file.xlsx";

			try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
				workbook.write(fileOut);
			}
			String fileName = "秩序表-名单（队伍）-" + gameId + ".xlsx";
			String fileUrl = updateToOss(filePath, fileName);
			log.info("Excel文件已生成！");

			return ApiResponse.success(fileUrl);

		}
		catch (Exception e) {
			log.error("export error", e);
			return ApiResponse.error("导出失败");

		}
	}

}

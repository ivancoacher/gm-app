package com.jsnjwj.facade.service.v2.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson2.JSON;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.dao.SessionDrawDao;
import com.jsnjwj.facade.entity.ArrangeAreaSessionEntity;
import com.jsnjwj.facade.entity.GameAreaEntity;
import com.jsnjwj.facade.entity.GameSessionEntity;
import com.jsnjwj.facade.entity.SignSingleEntity;
import com.jsnjwj.facade.enums.DrawTypeEnum;
import com.jsnjwj.facade.enums.ItemTypeEnum;
import com.jsnjwj.facade.manager.ArrangeAreaSessionManager;
import com.jsnjwj.facade.manager.ArrangeSessionItemManager;
import com.jsnjwj.facade.manager.ArrangeSessionManager;
import com.jsnjwj.facade.mapper.ArrangeAreaSessionMapper;
import com.jsnjwj.facade.mapper.GameAreaMapper;
import com.jsnjwj.facade.mapper.GameDrawMapper;
import com.jsnjwj.facade.mapper.SignSingleMapper;
import com.jsnjwj.facade.service.v2.ArrangeDrawExportService;
import com.jsnjwj.facade.vo.AreaSessionVo;
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
public class ArrangeDrawExportServiceImpl implements ArrangeDrawExportService {

	private final ArrangeSessionManager arrangeSessionManager;

	private final ArrangeSessionItemManager arrangeSessionItemManager;

	private final GameDrawMapper gameDrawMapper;

	private final SignSingleMapper signSingleMapper;

	private final GameAreaMapper gameAreaMapper;

	private final ArrangeAreaSessionManager arrangeAreaSessionManager;

	/**
	 * 导出场次秩序册
	 * @param gameId
	 * @return
	 */
	@Override
	public ApiResponse<?> exportSession(Long gameId) {
		try {
			// 查询所有场次
			String filePath = "./file.xlsx";

			List<GameSessionEntity> sessionEntityList = arrangeSessionManager.getListByGameId(gameId);
			if (CollectionUtil.isEmpty(sessionEntityList)) {
				throw new Exception("请先设置场次");
			}
			if (CollectionUtil.isNotEmpty(sessionEntityList)) {
				Workbook workbook = new XSSFWorkbook();

				for (GameSessionEntity sessionEntity : sessionEntityList) {
					// 创建工作簿
					Sheet sheet = workbook.createSheet(sessionEntity.getSessionName());

					// 创建行
					Row row = sheet.createRow(0);

					// 设置字体样式
					Font font = workbook.createFont();
					font.setFontName("Arial");
					font.setFontHeightInPoints((short) 12);
					font.setBold(true);
					// 设置单元格样式
					CellStyle cellStyle = workbook.createCellStyle();
					cellStyle.setAlignment(HorizontalAlignment.CENTER);
					cellStyle.setFont(font);

					// 设置行间距为1倍
					row.setHeightInPoints(16);

					// 设置垂直对齐方式为垂直居中
					cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
					cellStyle.setWrapText(true); // 设置自动换行

					// 创建场次名
					Cell cell = row.createCell(0);
					cell.setCellValue(sessionEntity.getSessionName());
					cell.setCellStyle(cellStyle);
					addMergedRegion(sheet, 0, 0, 0, 4);

					// 设置单元格宽度
					sheet.setColumnWidth(0, 12 * 256); // 设置列宽度
					sheet.setColumnWidth(1, 40 * 256);
					sheet.setColumnWidth(2, 12 * 256); // 设置列宽度
					sheet.setColumnWidth(3, 12 * 256); // 设置列宽度
					sheet.setColumnWidth(4, 12 * 256); // 设置列宽度

					// 设置整个表格的宽度适配 A4 纸张的宽度
					sheet.setFitToPage(true);
					PrintSetup printSetup = sheet.getPrintSetup();
					printSetup.setFitWidth((short) 1); // 将 Fit Width 设置为 1
					printSetup.setFitHeight((short) 0); // 将 Fit Width 设置为 1
					sheet.setFitToPage(true);
					// 新增空白行
					int i = 1;

					Font contentFont = workbook.createFont();
					contentFont.setFontName("Arial");
					contentFont.setFontHeightInPoints((short) 11);
					CellStyle contentCellStyle = workbook.createCellStyle();
					contentCellStyle.setAlignment(HorizontalAlignment.CENTER);
					contentCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
					contentCellStyle.setWrapText(true); // 设置自动换行
					contentCellStyle.setFont(contentFont);

					// 查询该场次内所有项目
					List<SessionItemVo> sessionItemList = arrangeSessionItemManager.fetchBySessionId(gameId,
							sessionEntity.getId());
					if (CollectionUtil.isNotEmpty(sessionItemList)) {

						// 查询每个项目内的所有报名信息
						List<SessionDrawDao> itemEntities = gameDrawMapper.getBySessionNo(gameId,
								sessionEntity.getId());

						for (SessionItemVo sessionItemVo : sessionItemList) {
							Row itemRow = sheet.createRow(i);
							itemRow.setHeightInPoints(24);
							Font groupFont = workbook.createFont();
							groupFont.setFontName("Arial");
							groupFont.setFontHeightInPoints((short) 12);
							groupFont.setBold(true);
							CellStyle itemCellStyle = workbook.createCellStyle();
							itemCellStyle.setAlignment(HorizontalAlignment.CENTER);
							itemCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
							itemCellStyle.setWrapText(true); // 设置自动换行
							itemCellStyle.setFont(groupFont);

							Font titleFont = workbook.createFont();
							titleFont.setFontName("Arial");
							titleFont.setFontHeightInPoints((short) 12);
							titleFont.setBold(true);

							CellStyle titleCellStyle = workbook.createCellStyle();
							titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
							titleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
							titleCellStyle.setWrapText(true); // 设置自动换行
							titleCellStyle.setFont(titleFont);

							Cell itemCell = itemRow.createCell(0);
							itemCell.setCellValue(sessionItemVo.getGroupName() + " " + sessionItemVo.getItemName());
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

							for (SessionDrawDao sessionDrawDao : itemEntities) {
								if (!Objects.equals(sessionDrawDao.getSessionId(), sessionItemVo.getSessionId())) {
									continue;
								}

								if (!Objects.equals(sessionDrawDao.getItemId(), sessionItemVo.getItemId())) {
									continue;
								}

								Row contentRow = sheet.createRow(i);

								Cell contentCell1 = contentRow.createCell(0);
								contentCell1.setCellValue(sessionDrawDao.getSort());
								contentCell1.setCellStyle(contentCellStyle);

								Cell contentCell2 = contentRow.createCell(1);

								if (Objects.equals(sessionDrawDao.getDrawType(), ItemTypeEnum.TYPE_SINGLE.getType())) {
									contentCell2.setCellValue(sessionDrawDao.getSignName());
								}
								else {
									// 根据队伍获取所有参赛选手
									Long teamId = sessionDrawDao.getTeamId();
									List<SignSingleEntity> signSingleEntities = signSingleMapper
										.selectList(new LambdaQueryWrapper<SignSingleEntity>()
											.eq(SignSingleEntity::getTeamId, teamId));
									String teamMates = signSingleEntities.stream()
										.map(SignSingleEntity::getName)
										.collect(Collectors.joining(","));
									contentCell2.setCellValue(teamMates);
								}

								contentCell2.setCellStyle(contentCellStyle);

								Cell contentCell3 = contentRow.createCell(2);
								contentCell3.setCellValue(sessionDrawDao.getTeamName());
								contentCell3.setCellStyle(contentCellStyle);

								Cell contentCell4 = contentRow.createCell(3);
								contentCell4.setCellValue(sessionDrawDao.getSignOrg());
								contentCell4.setCellStyle(contentCellStyle);

								Cell contentCell5 = contentRow.createCell(4);
								contentCell5
									.setCellValue(sessionDrawDao.getGroupName() + " " + sessionDrawDao.getItemName());
								contentCell5.setCellStyle(contentCellStyle);
								i++;
							}
							i++;
						}
					}

				}
				try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
					workbook.write(fileOut);
				}
			}

			String fileName = "抽签结果-" + gameId + ".xlsx";
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
	 * 导出场地秩序册
	 * @param gameId
	 * @return
	 */
	@Override
	public ApiResponse<?> exportAreaSession(Long gameId) {

		try {
			// 查询所有场次
			String filePath = "./file.xlsx";

			// 获取所有场地
			List<GameAreaEntity> gameAreaList = gameAreaMapper
				.selectList(new LambdaQueryWrapper<GameAreaEntity>().eq(GameAreaEntity::getGameId, gameId));
			if (CollectionUtil.isEmpty(gameAreaList)) {
				return ApiResponse.error("请先设置场地");
			}
			Workbook workbook = new XSSFWorkbook();

			for (GameAreaEntity gameAreaEntity : gameAreaList) {
				// 获取该场地下所有场次
				List<AreaSessionVo> arrangeAreaSessionEntities = arrangeAreaSessionManager.getSessionByAreaId(gameId,
						gameAreaEntity.getId());

				if (CollectionUtil.isNotEmpty(arrangeAreaSessionEntities)) {

					// 创建工作簿
					Sheet sheet = workbook.createSheet(gameAreaEntity.getAreaName());

					List<GameSessionEntity> sessionEntityList = arrangeSessionManager.getListByGameId(gameId);
					if (CollectionUtil.isEmpty(sessionEntityList)) {
						throw new Exception("请先设置场次");
					}
					if (CollectionUtil.isNotEmpty(sessionEntityList)) {
						int i = 1;
						for (GameSessionEntity sessionEntity : sessionEntityList) {

							// 创建行
							Row row = sheet.createRow(i);

							// 设置字体样式
							Font font = workbook.createFont();
							font.setFontName("Arial");
							font.setFontHeightInPoints((short) 12);
							font.setBold(true);
							// 设置单元格样式
							CellStyle cellStyle = workbook.createCellStyle();
							cellStyle.setAlignment(HorizontalAlignment.CENTER);
							cellStyle.setFont(font);

							// 设置行间距为1倍
							row.setHeightInPoints(16);

							// 设置垂直对齐方式为垂直居中
							cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
							cellStyle.setWrapText(true); // 设置自动换行

							// 创建场次名
							Cell cell = row.createCell(0);
							cell.setCellValue(sessionEntity.getSessionName());
							cell.setCellStyle(cellStyle);
							addMergedRegion(sheet, i, i, 0, 4);

							// 设置单元格宽度
							sheet.setColumnWidth(0, 12 * 256); // 设置列宽度
							sheet.setColumnWidth(1, 40 * 256);
							sheet.setColumnWidth(2, 12 * 256); // 设置列宽度
							sheet.setColumnWidth(3, 12 * 256); // 设置列宽度
							sheet.setColumnWidth(4, 12 * 256); // 设置列宽度

							// 设置整个表格的宽度适配 A4 纸张的宽度
							sheet.setFitToPage(true);
							PrintSetup printSetup = sheet.getPrintSetup();
							printSetup.setFitWidth((short) 1); // 将 Fit Width 设置为 1
							printSetup.setFitHeight((short) 0); // 将 Fit Width 设置为 1
							sheet.setFitToPage(true);
							// 新增空白行
							i++;

							Font contentFont = workbook.createFont();
							contentFont.setFontName("Arial");
							contentFont.setFontHeightInPoints((short) 11);
							CellStyle contentCellStyle = workbook.createCellStyle();
							contentCellStyle.setAlignment(HorizontalAlignment.CENTER);
							contentCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
							contentCellStyle.setWrapText(true); // 设置自动换行
							contentCellStyle.setFont(contentFont);

							// 查询该场次内所有项目
							List<SessionItemVo> sessionItemList = arrangeSessionItemManager.fetchBySessionId(gameId,
									sessionEntity.getId());
							if (CollectionUtil.isNotEmpty(sessionItemList)) {

								// 查询每个项目内的所有报名信息
								List<SessionDrawDao> itemEntities = gameDrawMapper.getBySessionNo(gameId,
										sessionEntity.getId());

								for (SessionItemVo sessionItemVo : sessionItemList) {
									Row itemRow = sheet.createRow(i);
									itemRow.setHeightInPoints(24);
									Font groupFont = workbook.createFont();
									groupFont.setFontName("Arial");
									groupFont.setFontHeightInPoints((short) 12);
									groupFont.setBold(true);
									CellStyle itemCellStyle = workbook.createCellStyle();
									itemCellStyle.setAlignment(HorizontalAlignment.CENTER);
									itemCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
									itemCellStyle.setWrapText(true); // 设置自动换行
									itemCellStyle.setFont(groupFont);

									Font titleFont = workbook.createFont();
									titleFont.setFontName("Arial");
									titleFont.setFontHeightInPoints((short) 12);
									titleFont.setBold(true);

									CellStyle titleCellStyle = workbook.createCellStyle();
									titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
									titleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
									titleCellStyle.setWrapText(true); // 设置自动换行
									titleCellStyle.setFont(titleFont);

									Cell itemCell = itemRow.createCell(0);
									itemCell
										.setCellValue(sessionItemVo.getGroupName() + " " + sessionItemVo.getItemName());
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

									for (SessionDrawDao sessionDrawDao : itemEntities) {
										if (!Objects.equals(sessionDrawDao.getSessionId(),
												sessionItemVo.getSessionId())) {
											continue;
										}

										if (!Objects.equals(sessionDrawDao.getItemId(), sessionItemVo.getItemId())) {
											continue;
										}

										Row contentRow = sheet.createRow(i);

										Cell contentCell1 = contentRow.createCell(0);
										contentCell1.setCellValue(sessionDrawDao.getSort());
										contentCell1.setCellStyle(contentCellStyle);

										Cell contentCell2 = contentRow.createCell(1);

										if (Objects.equals(sessionDrawDao.getDrawType(),
												ItemTypeEnum.TYPE_SINGLE.getType())) {
											contentCell2.setCellValue(sessionDrawDao.getSignName());
										}
										else {
											// 根据队伍获取所有参赛选手
											Long teamId = sessionDrawDao.getTeamId();
											List<SignSingleEntity> signSingleEntities = signSingleMapper
												.selectList(new LambdaQueryWrapper<SignSingleEntity>()
													.eq(SignSingleEntity::getTeamId, teamId));
											String teamMates = signSingleEntities.stream()
												.map(SignSingleEntity::getName)
												.collect(Collectors.joining(","));
											contentCell2.setCellValue(teamMates);
										}

										contentCell2.setCellStyle(contentCellStyle);

										Cell contentCell3 = contentRow.createCell(2);
										contentCell3.setCellValue(sessionDrawDao.getTeamName());
										contentCell3.setCellStyle(contentCellStyle);

										Cell contentCell4 = contentRow.createCell(3);
										contentCell4.setCellValue(sessionDrawDao.getSignOrg());
										contentCell4.setCellStyle(contentCellStyle);

										Cell contentCell5 = contentRow.createCell(4);
										contentCell5.setCellValue(
												sessionDrawDao.getGroupName() + " " + sessionDrawDao.getItemName());
										contentCell5.setCellStyle(contentCellStyle);
										i++;
									}
									i++;
								}
								i++;
							}
							addBlankRow(sheet, i, 4);
							i++;

						}
						try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
							workbook.write(fileOut);
						}
					}

				}

			}

			String fileName = "场序表-" + gameId + ".xlsx";
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

}

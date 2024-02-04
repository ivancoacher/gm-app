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
import java.util.List;
import java.util.stream.Collectors;

/**
 * 合同比对service
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SignApplyExportServiceImpl implements SignApplyExportService {

    private final SignApplyManager signApplyManager;

    @Override
    public ApiResponse<?> exportSignProgram(SignSingleProgramExportQuery request) {
        Long gameId = request.getGameId();
        String exportType = request.getType();
        if ("type_item".equals(exportType)) {
            return exportByItem(gameId);
        } else if ("type_group".equals(exportType)) {
            return exportByGroup(gameId);
        } else {
            return exportAll(gameId);
        }

    }

    /**
     * 根据组别导出
     *
     * @param gameId
     * @return
     */
    private ApiResponse<?> exportByGroup(Long gameId) {
        try {
            // 创建工作簿
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Sheet1");

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
            List<SignSingleEntity> groupEntities = signApplyManager.getSignGroups(gameId);
            if (CollUtil.isNotEmpty(groupEntities)) {
                int orgCode = 1;

                for (SignSingleEntity signSingleEntity : groupEntities) {
                    // 填充group信息
                    GameGroupEntity groupEntity = signApplyManager.getGroupById(signSingleEntity.getGroupId());
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

                    Long groupId = signSingleEntity.getGroupId();
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
            String fileName = "秩序表-组别.xlsx";
            String fileUrl = updateToOss(filePath, fileName);
            log.info("Excel文件已生成！");

            return ApiResponse.success(fileUrl);

        } catch (Exception e) {
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

    /**
     * 上传导出文件到oss
     *
     * @param fileName
     * @param orgName
     * @return
     * @throws IOException
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
     *
     * @param workbook
     * @param sheet
     * @param i
     * @param columnTitleName
     */
    private void addPlayerTitleRow(Workbook workbook, Sheet sheet, int i, String columnTitleName) {
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

        titleCellCode.setCellValue(columnTitleName + ":");
        titleCellCode.setCellStyle(teamCellStyle);

        addMergedRegion(sheet, i, i, 0, 9);

    }

    private void addPlayerContentRow(Workbook workbook, Sheet sheet, int rowId, int cellId, String columnContent) {
        Row playerRow = sheet.getRow(rowId); // 获取当前行
        if (playerRow == null) {
            playerRow = sheet.createRow(rowId);
        }
        playerRow.setHeightInPoints(20);

        Font orgFont = workbook.createFont();
        orgFont.setFontName("Arial");
        orgFont.setFontHeightInPoints((short) 11);

        CellStyle teamCellStyle = workbook.createCellStyle();
        teamCellStyle.setAlignment(HorizontalAlignment.CENTER);
        teamCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        teamCellStyle.setFont(orgFont);

        // 编号
        Cell titleCellCode = playerRow.createCell(cellId);

        titleCellCode.setCellValue(columnContent);
        teamCellStyle.setWrapText(true); // 设置自动换行

        titleCellCode.setCellStyle(teamCellStyle);

    }

    /**
     * 绘制教练+领队信息
     *
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

    private void addMergedRegion(Sheet sheet, int startRow, int endRow, int startCol, int endCol) {
        sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, startCol, endCol));
    }

    /**
     * 根据项目导出
     *
     * @param gameId
     * @return
     */
    private ApiResponse<?> exportByItem(Long gameId) {
        try {
            // 创建工作簿
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Sheet1");

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
            String fileName = "秩序表-项目.xlsx";
            String fileUrl = updateToOss(filePath, fileName);
            log.info("Excel文件已生成！");

            return ApiResponse.success(fileUrl);

        } catch (Exception e) {
            log.error("export error", e);
            return ApiResponse.error("导出失败");

        }
    }

    private ApiResponse<?> exportAll(Long gameId) {
        try {
            // 创建工作簿
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Sheet1");

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
            String fileName = "秩序表-名单.xlsx";
            String fileUrl = updateToOss(filePath, fileName);
            log.info("Excel文件已生成！");

            return ApiResponse.success(fileUrl);

        } catch (Exception e) {
            log.error("export error", e);
            return ApiResponse.error("导出失败");

        }
    }

}

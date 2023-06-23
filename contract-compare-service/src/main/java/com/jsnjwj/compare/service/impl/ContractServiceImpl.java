package com.jsnjwj.compare.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.jsnjwj.compare.dao.CContractFileDao;
import com.jsnjwj.compare.dao.CContractFilePageDao;
import com.jsnjwj.compare.dao.CContractRecordDao;
import com.jsnjwj.compare.dao.CContractResultDao;
import com.jsnjwj.compare.entity.CContractFile;
import com.jsnjwj.compare.entity.CContractFilePage;
import com.jsnjwj.compare.entity.CContractRecord;
import com.jsnjwj.compare.entity.CContractResult;
import com.jsnjwj.compare.service.ContractCommonService;
import com.jsnjwj.compare.service.ContractService;
import com.jsnjwj.compare.utils.FileUtils;
import com.jsnjwj.compare.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 合同比对service
 */
@Service
@Slf4j
public class ContractServiceImpl implements ContractService {

    @Resource
    private CContractRecordDao cContractRecordDao;

    @Resource
    private CContractFileDao cContractFileDao;

    @Resource
    private CContractFilePageDao cContractFilePageDao;

    @Resource
    private CContractResultDao cContractResultDao;

    @Resource
    private ContractCommonService contractCommonService;
    /**
     * 合同比对执行
     */
    @Override
    public void compare(MultipartFile sourceFile, MultipartFile compareFile) throws Exception {
        Integer userId = 1;
        // 1、上传文档
        File sourceFilePath = FileUtils.uploadFile(sourceFile);
        File compareFilePath = FileUtils.uploadFile(compareFile);

        // 保存对比文件
        CContractFile sourceFileEntity = new CContractFile();
        sourceFileEntity.setFileName(sourceFile.getOriginalFilename());
        sourceFileEntity.setFilePath(sourceFilePath.getPath());
        sourceFileEntity.setFileType(sourceFile.getContentType());
        sourceFileEntity.setCreateTime(new Date());

        Integer sourceFileId = cContractFileDao.insertOne(sourceFileEntity);
        CContractFile compareFileEntity = new CContractFile();
        compareFileEntity.setFileName(compareFile.getOriginalFilename());
        compareFileEntity.setFilePath(compareFilePath.getPath());
        compareFileEntity.setFileType(compareFile.getContentType());
        compareFileEntity.setCreateTime(new Date());

        Integer compareFileId = cContractFileDao.insertOne(compareFileEntity);

        // 保存对比记录
        CContractRecord cContractRecord = new CContractRecord();
        cContractRecord.setUserId(userId);
        cContractRecord.setCompareFileId(compareFileId.longValue());
        cContractRecord.setOriginFileId(sourceFileId.longValue());
        cContractRecord.setCreateTime(new Date());
        cContractRecord.setUpdateTime(new Date());

        Integer recordId = cContractRecordDao.insert(cContractRecord);

        doCompare(recordId, sourceFilePath, sourceFileId);
        doCompare(recordId, compareFilePath, compareFileId);
    }

    @Async
    public void doCompare(Integer recordId, File sourceFilePath, Integer fileId) throws Exception {

        // 2、文档转图片
        List<Integer> sourceFilePagePathList = contractCommonService.saveFilePage(sourceFilePath, fileId);
        log.info(sourceFilePagePathList.toString());
        // 3、OCR识别文档
        List<CContractResult> sourceFileResultList = new ArrayList<>();
        for (Integer pageId : sourceFilePagePathList) {
            CContractFilePage pageInfo = cContractFilePageDao.selectById(pageId);
            String sourceFileResult = HttpUtils.getResp(pageInfo.getPagePath());
            log.info("sourceFile====pageId:{}===result{}", pageId, sourceFileResult);
            CContractResult result = new CContractResult();
            result.setContractId(Long.valueOf(recordId));
            result.setContent(sourceFileResult);
            result.setPageId(Long.valueOf(pageId));
            result.setCreateTime(new Date());
            sourceFileResultList.add(result);
        }
        cContractResultDao.insertBatch(sourceFileResultList);
    }


}

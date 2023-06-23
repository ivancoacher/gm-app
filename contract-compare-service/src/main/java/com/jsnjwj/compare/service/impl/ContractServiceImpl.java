package com.jsnjwj.compare.service.impl;

import com.jsnjwj.compare.dao.CContractFileDao;
import com.jsnjwj.compare.dao.CContractFilePageDao;
import com.jsnjwj.compare.dao.CContractRecordDao;
import com.jsnjwj.compare.dao.CContractResultDao;
import com.jsnjwj.compare.entity.CContractFile;
import com.jsnjwj.compare.entity.CContractRecord;
import com.jsnjwj.compare.service.ContractCommonService;
import com.jsnjwj.compare.service.ContractService;
import com.jsnjwj.compare.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;

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

        contractCommonService.doCompare(recordId, sourceFilePath, sourceFileId);
        contractCommonService.doCompare(recordId, compareFilePath, compareFileId);
    }

}

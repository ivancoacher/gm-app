package com.jsnjwj.compare.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.jsnjwj.compare.dao.CContractFileDao;
import com.jsnjwj.compare.dao.CContractFilePageDao;
import com.jsnjwj.compare.dao.CContractRecordDao;
import com.jsnjwj.compare.dao.CContractResultDao;
import com.jsnjwj.compare.entity.CContractFile;
import com.jsnjwj.compare.entity.CContractFilePage;
import com.jsnjwj.compare.entity.CContractRecord;
import com.jsnjwj.compare.service.ContractService;
import com.jsnjwj.compare.utils.FileUtils;
import com.jsnjwj.compare.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
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

    /**
     * 合同比对执行
     */
    @Override
    public void compare(MultipartFile sourceFile, MultipartFile compareFile) throws Exception {
        Integer userId = 1;
        // 1、上传文档
        File sourceFilePath = FileUtils.uploadFile(sourceFile);
        File compareFilePath = FileUtils.uploadFile(compareFile);
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

        CContractRecord cContractRecord = new CContractRecord();
        cContractRecord.setUserId(userId);
        cContractRecord.setCompareFileId(compareFileId.longValue());
        cContractRecord.setOriginFileId(sourceFileId.longValue());
        cContractRecord.setCreateTime(new Date());
        cContractRecord.setUpdateTime(new Date());
        Integer recordId = cContractRecordDao.insert(cContractRecord);


        // 2、文档转图片
        List<Integer> sourceFilePagePathList = saveFilePage(sourceFilePath, sourceFileId);
        List<Integer> compareFilePagePathList = saveFilePage(compareFilePath, sourceFileId);
        log.info(sourceFilePagePathList.toString());
        // 3、OCR识别文档
//        for (String path : sourceFilePagePathList) {
//            String sourceFileResult = HttpUtils.getResp(path);
//        }

//        for (String path : compareFilePagePathList) {
//            String compareFileResult = HttpUtils.getResp(path);
//        }

        // 4、文档对比


    }

    private List<Integer> saveFilePage(File compareFilePath, Integer sourceFileId) throws Exception {
        List<String> compareFilePagePathList = FileUtils.pdf2Image(compareFilePath);
        List<CContractFilePage> comparePageList = new ArrayList<>();

        for (int i = 1; i <= compareFilePagePathList.size(); i++) {
            CContractFilePage cContractFilePage = new CContractFilePage();
            cContractFilePage.setFileId(sourceFileId);
            cContractFilePage.setPageNo(i);
            cContractFilePage.setPagePath(compareFilePagePathList.get(i - 1));
            cContractFilePage.setCreateTime(new Date());
            cContractFilePage.setUpdateTime(new Date());
            comparePageList.add(cContractFilePage);
        }
        cContractFilePageDao.insertBatch(comparePageList);
        log.info(JSONArray.toJSONString(comparePageList));
        return comparePageList.stream().map(CContractFilePage::getId).collect(Collectors.toList());
    }

}

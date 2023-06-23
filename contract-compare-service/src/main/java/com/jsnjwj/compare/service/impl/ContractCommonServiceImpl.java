package com.jsnjwj.compare.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.jsnjwj.compare.dao.CContractFileDao;
import com.jsnjwj.compare.dao.CContractFilePageDao;
import com.jsnjwj.compare.entity.CContractFile;
import com.jsnjwj.compare.entity.CContractFilePage;
import com.jsnjwj.compare.service.ContractCommonService;
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

@Service
@Slf4j
public class ContractCommonServiceImpl implements ContractCommonService {
    @Resource
    private CContractFilePageDao cContractFilePageDao;

    @Resource
    private CContractFileDao cContractFileDao;

    private List<CContractFilePage> saveFilePage(File compareFilePath, Integer sourceFileId) throws Exception {
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
        return comparePageList;
    }


    @Override
    @Async
    public void doCompare(Integer recordId, File sourceFilePath, Integer fileId) throws Exception {
        log.info("4");

        // 2、文档转图片
        List<CContractFilePage> sourceFilePagePathList = saveFilePage(sourceFilePath, fileId);
        log.info(sourceFilePagePathList.toString());
        // 3、OCR识别文档
        List<CContractFilePage> sourceFileResultList = new ArrayList<>();
        for (CContractFilePage pageInfo : sourceFilePagePathList) {
            String sourceFileResult = HttpUtils.getResp(pageInfo.getPagePath());
            pageInfo.setCompareResult(sourceFileResult);
            pageInfo.setContractId(recordId);
            sourceFileResultList.add(pageInfo);
        }
        cContractFilePageDao.insertOrUpdateBatch(sourceFileResultList);
    }

    @Override
    public Integer saveFilePath(MultipartFile file, File filePath) {
        CContractFile compareFileEntity = new CContractFile();
        compareFileEntity.setFileName(file.getOriginalFilename());
        compareFileEntity.setFilePath(filePath.getPath());
        compareFileEntity.setFileType(file.getContentType());
        compareFileEntity.setCreateTime(new Date());
        cContractFileDao.insertOne(compareFileEntity);
        return compareFileEntity.getId();
    }

}

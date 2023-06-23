package com.jsnjwj.compare.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.jsnjwj.compare.dao.CContractFilePageDao;
import com.jsnjwj.compare.dao.CContractResultDao;
import com.jsnjwj.compare.entity.CContractFilePage;
import com.jsnjwj.compare.entity.CContractResult;
import com.jsnjwj.compare.service.ContractCommonService;
import com.jsnjwj.compare.utils.FileUtils;
import com.jsnjwj.compare.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ContractCommonServiceImpl implements ContractCommonService {
    @Resource
    private CContractFilePageDao cContractFilePageDao;

    @Resource
    private CContractResultDao cContractResultDao;

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


    @Override
    @Async
    public void doCompare(Integer recordId, File sourceFilePath, Integer fileId) throws Exception {

        // 2、文档转图片
        List<Integer> sourceFilePagePathList = saveFilePage(sourceFilePath, fileId);
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

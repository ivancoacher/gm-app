package com.jsnjwj.compare.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.jsnjwj.compare.dao.CContractFilePageDao;
import com.jsnjwj.compare.entity.CContractFilePage;
import com.jsnjwj.compare.service.ContractCommonService;
import com.jsnjwj.compare.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public List<Integer> saveFilePage(File compareFilePath, Integer sourceFileId) throws Exception {
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

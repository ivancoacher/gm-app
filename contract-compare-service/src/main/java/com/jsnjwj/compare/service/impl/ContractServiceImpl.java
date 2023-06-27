package com.jsnjwj.compare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.compare.dao.CContractFilePageDao;
import com.jsnjwj.compare.dao.CContractRecordDao;
import com.jsnjwj.compare.entity.CContractFilePage;
import com.jsnjwj.compare.entity.CContractRecord;
import com.jsnjwj.compare.enums.CompareStateEnum;
import com.jsnjwj.compare.query.ComparePagesQuery;
import com.jsnjwj.compare.query.CompareResultQuery;
import com.jsnjwj.compare.query.ContractDetailQuery;
import com.jsnjwj.compare.query.ContractListQuery;
import com.jsnjwj.compare.service.ContractCommonService;
import com.jsnjwj.compare.service.ContractService;
import com.jsnjwj.compare.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 合同比对service
 */
@Service
@Slf4j
public class ContractServiceImpl implements ContractService {

    @Resource
    private CContractRecordDao cContractRecordDao;


    @Resource
    private CContractFilePageDao cContractFilePageDao;


    @Resource
    private ContractCommonService contractCommonService;

    /**
     * 合同比对执行
     *
     * @return
     */
    @Override
    public ApiResponse compare(MultipartFile sourceFile, MultipartFile compareFile) throws Exception {
        Integer userId = 1;
        // 1、上传文档
        Map<String, Object> sourceFileMap = FileUtils.uploadFile(sourceFile);
        Map<String, Object> compareFileMap = FileUtils.uploadFile(compareFile);
        File sourceFilePath = (File) sourceFileMap.get("file");
        File compareFilePath = (File) compareFileMap.get("file");
        // 保存对比文件
        Integer sourceFileId = contractCommonService.saveFilePath(sourceFile, (String) sourceFileMap.get("location"));

        Integer compareFileId = contractCommonService.saveFilePath(compareFile, (String) compareFileMap.get("location"));

        // 保存对比记录
        CContractRecord cContractRecord = new CContractRecord();
        cContractRecord.setUserId(userId);
        cContractRecord.setCompareFileId(compareFileId.longValue());
        cContractRecord.setCompareFileName(compareFile.getOriginalFilename());
        cContractRecord.setOriginFileId(sourceFileId.longValue());
        cContractRecord.setOriginFileName(sourceFile.getOriginalFilename());
        cContractRecord.setCreateTime(new Date());
        cContractRecord.setUpdateTime(new Date());
        cContractRecord.setCompareState(CompareStateEnum.HANDLING);
        cContractRecordDao.insert(cContractRecord);
        Integer recordId = cContractRecord.getId();
        contractCommonService.doCompare(recordId, sourceFilePath, sourceFileId);
        contractCommonService.doCompare(recordId, compareFilePath, compareFileId);
        return ApiResponse.success(recordId);
    }

    /**
     * 查询合同对比列表
     *
     * @param query
     * @return
     */
    @Override
    public ApiResponse<Page<CContractRecord>> queryList(ContractListQuery query) {
        ApiResponse<Page<CContractRecord>> response = new ApiResponse<>();

        Page<CContractRecord> resultPage = new Page<>();
        resultPage.setCurrent(query.getPageIndex());
        resultPage.setSize(query.getPageSize());
        QueryWrapper<CContractRecord> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(CContractRecord::getUserId, query.getUserId());
        resultPage = cContractRecordDao.selectPage(resultPage, wrapper);

        response.setCode(20000);
        response.setMessage("查询成功");
        response.setData(resultPage);
        return response;
    }

    /**
     * 查询合同对比详情
     *
     * @param query
     * @return
     */
    @Override
    public ApiResponse<CContractRecord> queryDetail(ContractDetailQuery query) {
        ApiResponse<CContractRecord> response = new ApiResponse<>();
        CContractRecord result = cContractRecordDao.selectById(query.getContractId());
        response.setCode(20000);
        response.setMessage("查询成功");
        response.setData(result);
        return response;
    }

    /**
     * 查询对比结果
     *
     * @param query
     * @return
     */
    @Override
    public ApiResponse<CContractFilePage> queryResult(CompareResultQuery query) {
        ApiResponse<CContractFilePage> response = new ApiResponse<>();
        QueryWrapper<CContractFilePage> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(CContractFilePage::getContractId, query.getContractId())
                .eq(CContractFilePage::getPageNo, query.getPageNo())
                .eq(CContractFilePage::getFileId, query.getFileId());
        CContractFilePage result = cContractFilePageDao.selectOne(wrapper);
        response.setCode(20000);
        response.setMessage("查询成功");
        response.setData(result);
        return response;
    }

    @Override
    public ApiResponse<List<CContractFilePage>> queryPages(ComparePagesQuery query) {
        ApiResponse<List<CContractFilePage>> response = new ApiResponse<>();
        QueryWrapper<CContractFilePage> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(CContractFilePage::getContractId, query.getContractId());
        wrapper.lambda().eq(CContractFilePage::getFileId, query.getFileId());
        wrapper.lambda().orderByAsc(CContractFilePage::getPageNo);
        List<CContractFilePage> result = cContractFilePageDao.selectList(wrapper);
        response.setCode(20000);
        response.setMessage("查询成功");
        response.setData(result);
        return response;
    }
}

package com.jsnjwj.compare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.compare.dao.CContractFilePageDao;
import com.jsnjwj.compare.dao.CContractRecordDao;
import com.jsnjwj.compare.entity.CContractFilePageEntity;
import com.jsnjwj.compare.entity.CContractRecordEntity;
import com.jsnjwj.compare.enums.CompareStateEnum;
import com.jsnjwj.compare.query.*;
import com.jsnjwj.compare.response.CompareAnalysisChartResponse;
import com.jsnjwj.compare.response.CompareAnalysisResponse;
import com.jsnjwj.compare.service.ContractCommonService;
import com.jsnjwj.compare.service.ContractService;
import com.jsnjwj.compare.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
    private CContractFilePageDao cContractFilePageDao;

    @Resource
    private ContractCommonService contractCommonService;

    /**
     * 合同比对执行
     *
     * @return
     */
    @Override
    public ApiResponse compare(ContractCompareQuery query, MultipartFile sourceFile, MultipartFile compareFile)
            throws Exception {
        Integer userId = query.getUserId();
        // 保存记录
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String tradeNo = UUID.randomUUID().toString();
        CContractRecordEntity cContractRecordEntity = new CContractRecordEntity();
        cContractRecordEntity.setUserId(userId);
        cContractRecordEntity.setCompareFileName(compareFile.getOriginalFilename());
        cContractRecordEntity.setOriginFileName(sourceFile.getOriginalFilename());
        cContractRecordEntity.setCreateTime(new Date());
        cContractRecordEntity.setUpdateTime(new Date());
        cContractRecordEntity.setOriginFileId(0L);
        cContractRecordEntity.setCompareFileId(0L);
        cContractRecordEntity.setTradeNo(tradeNo);
        cContractRecordEntity.setOperateDay(dateTime.format(formatter));
        cContractRecordEntity.setCompareState(CompareStateEnum.HANDLING.getCompareState());
        cContractRecordDao.insert(cContractRecordEntity);

        // 1、上传文档
        Map<String, Object> sourceFileMap = FileUtils.uploadFile(sourceFile);
        Map<String, Object> compareFileMap = FileUtils.uploadFile(compareFile);
        File sourceFilePath = (File) sourceFileMap.get("file");
        File compareFilePath = (File) compareFileMap.get("file");
        // 保存对比文件
        Integer sourceFileId = contractCommonService.saveFilePath(sourceFile, (String) sourceFileMap.get("location"));

        Integer compareFileId = contractCommonService.saveFilePath(compareFile,
                (String) compareFileMap.get("location"));

        // 更新对比记录
        cContractRecordEntity.setOriginFileId(Long.valueOf(sourceFileId));
        cContractRecordEntity.setCompareFileId(Long.valueOf(compareFileId));
        cContractRecordEntity.setUpdateTime(new Date());
        cContractRecordDao.updateById(cContractRecordEntity);

        Integer recordId = cContractRecordEntity.getId();
        contractCommonService.doCompare(recordId, sourceFilePath, sourceFileId);
        contractCommonService.doCompare(recordId, compareFilePath, compareFileId);
        return ApiResponse.success(tradeNo);
    }

    /**
     * 查询合同对比列表
     *
     * @param query
     * @return
     */
    @Override
    public ApiResponse<Page<CContractRecordEntity>> queryList(ContractListQuery query) {
        ApiResponse<Page<CContractRecordEntity>> response = new ApiResponse<>();

        Page<CContractRecordEntity> resultPage = new Page<>();
        resultPage.setCurrent(query.getPageIndex());
        resultPage.setSize(query.getPageSize());
        LambdaQueryWrapper<CContractRecordEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CContractRecordEntity::getUserId, query.getUserId());

        // 筛选时间
        wrapper.gt(StringUtils.isNotEmpty(query.getStartTime()), CContractRecordEntity::getCreateTime,
                query.getStartTime());
        wrapper.lt(StringUtils.isNotEmpty(query.getEndTime()), CContractRecordEntity::getCreateTime,
                query.getEndTime() + " 23:59:59");

        // 根据文件名查询
        wrapper.and(StringUtils.isNotEmpty(query.getTitle()), wrap -> wrap.like(CContractRecordEntity::getCompareFileName, query.getTitle())
                .or()
                .like(CContractRecordEntity::getOriginFileName, query.getTitle()));

        // 比对状态
        wrapper.eq(!Objects.isNull(query.getCompareState()), CContractRecordEntity::getCompareState, query.getCompareState());
        // 排序
        wrapper.orderByDesc(CContractRecordEntity::getCreateTime);
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
    public ApiResponse<CContractRecordEntity> queryDetail(ContractDetailQuery query) {
        ApiResponse<CContractRecordEntity> response = new ApiResponse<>();
        CContractRecordEntity result = cContractRecordDao.selectById(query.getContractId());
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
    public ApiResponse<CContractFilePageEntity> queryResult(CompareResultQuery query) {
        ApiResponse<CContractFilePageEntity> response = new ApiResponse<>();
        QueryWrapper<CContractFilePageEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(CContractFilePageEntity::getContractId, query.getContractId())
                .eq(CContractFilePageEntity::getPageNo, query.getPageNo())
                .eq(CContractFilePageEntity::getFileId, query.getFileId());
        CContractFilePageEntity result = cContractFilePageDao.selectOne(wrapper);
        response.setCode(20000);
        response.setMessage("查询成功");
        response.setData(result);
        return response;
    }

    @Override
    public ApiResponse<List<CContractFilePageEntity>> queryPages(ComparePagesQuery query) {
        ApiResponse<List<CContractFilePageEntity>> response = new ApiResponse<>();
        QueryWrapper<CContractFilePageEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(CContractFilePageEntity::getContractId, query.getContractId());
        wrapper.lambda().eq(CContractFilePageEntity::getFileId, query.getFileId());
        wrapper.lambda().orderByAsc(CContractFilePageEntity::getPageNo);
        List<CContractFilePageEntity> result = cContractFilePageDao.selectList(wrapper);
        response.setCode(20000);
        response.setMessage("查询成功");
        response.setData(result);
        return response;
    }

    @Override
    public ApiResponse<CompareAnalysisResponse> queryAnalysis(Long userId) {
        CompareAnalysisResponse response = new CompareAnalysisResponse();

        // 总计
        QueryWrapper<CContractRecordEntity> wrapper1 = new QueryWrapper<>();
        wrapper1.lambda().eq(CContractRecordEntity::getUserId, userId);
        Long totalCount = cContractRecordDao.selectCount(wrapper1);

        // 处理成功
        QueryWrapper<CContractRecordEntity> wrapper2 = new QueryWrapper<>();
        wrapper2.lambda().eq(CContractRecordEntity::getUserId, userId);
        wrapper2.lambda().eq(CContractRecordEntity::getCompareState, CompareStateEnum.HANDLE_SUCCESS.getCompareState());
        Long successCount = cContractRecordDao.selectCount(wrapper2);

        // 处理失败
        QueryWrapper<CContractRecordEntity> wrapper3 = new QueryWrapper<>();
        wrapper3.lambda().eq(CContractRecordEntity::getUserId, userId);
        wrapper3.lambda().eq(CContractRecordEntity::getCompareState, CompareStateEnum.HANDLE_FAIL.getCompareState());
        Long failureCount = cContractRecordDao.selectCount(wrapper3);

        // 处理中
        QueryWrapper<CContractRecordEntity> wrapper4 = new QueryWrapper<>();
        wrapper4.lambda().eq(CContractRecordEntity::getUserId, userId);
        wrapper4.lambda()
                .and(wp -> wp.eq(CContractRecordEntity::getCompareState, CompareStateEnum.HANDLING.getCompareState())
                        .or()
                        .eq(CContractRecordEntity::getCompareState, CompareStateEnum.HANDLE_RETRY.getCompareState()));
        Long doingCount = cContractRecordDao.selectCount(wrapper4);

        response.setDoingCount(doingCount);
        response.setSuccessCount(successCount);
        response.setFailureCount(failureCount);
        response.setTotalCount(totalCount);
        return ApiResponse.success(response);
    }

    @Override
    public ApiResponse<List<CompareAnalysisChartResponse>> queryAnalysisChart(CompareAnalysisQuery query)
            throws ParseException {
        List<CompareAnalysisChartResponse> response = new ArrayList<>();
        String startTime = query.getStartTime();
        String endTime = query.getEndTime();

        response = initChartData(startTime, endTime);
        List<CompareAnalysisChartResponse> groupData = cContractRecordDao.selectGroupData(startTime, endTime);
        if (!groupData.isEmpty()) {
            Map<String, Object> groupDataDict = groupData.stream()
                    .collect(Collectors.toMap(CompareAnalysisChartResponse::getLabel,
                            CompareAnalysisChartResponse::getCount));
            response.forEach(rsp -> {
                if (groupDataDict.containsKey(rsp.getLabel())) {
                    rsp.setCount((Long) groupDataDict.get(rsp.getLabel()));
                }
            });
        }
        return ApiResponse.success(response);
    }

    private List<CompareAnalysisChartResponse> initChartData(String startTime, String endTime) throws ParseException {
        List<CompareAnalysisChartResponse> allDate = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date dBegin = sdf.parse(startTime);
        Date dEnd = sdf.parse(endTime);
        CompareAnalysisChartResponse startDate = new CompareAnalysisChartResponse();
        startDate.setLabel(sdf.format(dBegin.getTime()));
        allDate.add(startDate);
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime())) {
            CompareAnalysisChartResponse date = new CompareAnalysisChartResponse();
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            date.setLabel(sdf.format(calBegin.getTime()));
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            allDate.add(date);
        }
        return allDate;
    }

}

package com.jsnjwj.facade.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson2.JSON;
import com.jsnjwj.facade.dto.ImportSingleDto;
import com.jsnjwj.facade.manager.SignApplyManager;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
public class SingleImportListener implements ReadListener<ImportSingleDto> {

    private static final int BATCH_COUNT = 100;
    @Resource
    private SignApplyManager signApplyManager;
    private Long gameId;

    private List<ImportSingleDto> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    public SingleImportListener(Long gameId, SignApplyManager signApplyManager) {
        // TODO document why this constructor is empty
        this.gameId = gameId;
        this.signApplyManager = signApplyManager;
    }

    @Override
    public void invoke(ImportSingleDto tcSignTeam, AnalysisContext analysisContext) {
        log.info("解析到一条数据:{}", JSON.toJSONString(tcSignTeam));
        cachedDataList.add(tcSignTeam);

        if (cachedDataList.size() >= BATCH_COUNT) {
            signApplyManager.saveSingleBatch(this.gameId, cachedDataList);
            // 存储完成清理 list
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        signApplyManager.saveSingleBatch(this.gameId, cachedDataList);

    }

}

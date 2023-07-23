package com.jsnjwj.facade.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson2.JSON;
import com.jsnjwj.facade.dto.ImportTeamDto;
import com.jsnjwj.facade.entity.TcSignTeam;
import com.jsnjwj.facade.manager.SignApplyManager;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
public class TeamImportListener implements ReadListener<ImportTeamDto> {
    @Resource
    private SignApplyManager signApplyManager;
    private static final int BATCH_COUNT = 100;

    private Long gameId;
    private List<ImportTeamDto> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    public TeamImportListener(Long gameId,SignApplyManager signApplyManager) {
        // TODO document why this constructor is empty
        this.gameId = gameId;
        this.signApplyManager = signApplyManager;
    }
    @Override
    public void invoke(ImportTeamDto tcSignTeam, AnalysisContext analysisContext) {
        log.info("解析到一条数据:{}", JSON.toJSONString(tcSignTeam));
        cachedDataList.add(tcSignTeam);

        if (cachedDataList.size() >= BATCH_COUNT) {
            signApplyManager.saveTeamBatch(this.gameId,cachedDataList);
            // 存储完成清理 list
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        signApplyManager.saveTeamBatch(this.gameId,cachedDataList);

    }
}

package com.jsnjwj.facade.service;

import com.jsnjwj.facade.entity.GameItemEntity;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

public interface SignArrangeSequenceService {

    void syncArrangeRecord();

    @Async
    void syncArrangeRecord(GameItemEntity itemEntity);

    List<?> fetchList(Long gameId);

    int update();

}

package com.jsnjwj.facade.service;

import java.util.List;

public interface SignArrangeSequenceService {
    void syncArrangeRecord();

    List<?> fetchList(Long gameId);

    int update();
}

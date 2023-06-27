package com.jsnjwj.compare.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface ContractCommonService {
    @Async
    void doCompare(Integer recordId, File sourceFilePath, Integer fileId) throws Exception;

    Integer saveFilePath(MultipartFile file, String filePath);
}

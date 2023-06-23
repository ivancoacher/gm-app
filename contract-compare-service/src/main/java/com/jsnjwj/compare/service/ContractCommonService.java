package com.jsnjwj.compare.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface ContractCommonService {

    List<Integer> saveFilePage(File compareFilePath, Integer sourceFileId) throws Exception;
}

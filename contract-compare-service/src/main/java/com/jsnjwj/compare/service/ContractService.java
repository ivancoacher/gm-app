package com.jsnjwj.compare.service;

import org.springframework.web.multipart.MultipartFile;

public interface ContractService {

    void compare(MultipartFile sourceFile, MultipartFile compareFile) throws Exception;
}

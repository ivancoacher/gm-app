package com.jsnjwj.compare.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.compare.entity.CContractFilePage;
import com.jsnjwj.compare.entity.CContractRecord;
import com.jsnjwj.compare.query.ComparePagesQuery;
import com.jsnjwj.compare.query.CompareResultQuery;
import com.jsnjwj.compare.query.ContractDetailQuery;
import com.jsnjwj.compare.query.ContractListQuery;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ContractService {

	ApiResponse compare(MultipartFile sourceFile, MultipartFile compareFile) throws Exception;

	ApiResponse<Page<CContractRecord>> queryList(ContractListQuery query);

	ApiResponse<CContractRecord> queryDetail(ContractDetailQuery query);

	ApiResponse<CContractFilePage> queryResult(CompareResultQuery query);

	ApiResponse<List<CContractFilePage>> queryPages(ComparePagesQuery query);

}

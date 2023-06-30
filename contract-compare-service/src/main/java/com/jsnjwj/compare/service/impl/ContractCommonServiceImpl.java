package com.jsnjwj.compare.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.jsnjwj.compare.dao.CContractFileDao;
import com.jsnjwj.compare.dao.CContractFilePageDao;
import com.jsnjwj.compare.entity.CContractFile;
import com.jsnjwj.compare.entity.CContractFilePage;
import com.jsnjwj.compare.service.ContractCommonService;
import com.jsnjwj.compare.utils.FileUtils;
import com.jsnjwj.compare.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;

@Service
@Slf4j
public class ContractCommonServiceImpl implements ContractCommonService {

	@Resource
	private CContractFilePageDao cContractFilePageDao;

	@Resource
	private CContractFileDao cContractFileDao;

	private List<Map<String, Object>> saveFilePage(File compareFilePath, Integer sourceFileId) throws Exception {
		List<Map<String, Object>> compareFilePagePathList = FileUtils.pdf2Image(compareFilePath);
		List<CContractFilePage> comparePageList = new ArrayList<>();
		List<Map<String, Object>> resultList = new ArrayList<>();
		for (int i = 1; i <= compareFilePagePathList.size(); i++) {
			Map<String, Object> fileInfo = compareFilePagePathList.get(i - 1);

			CContractFilePage cContractFilePage = new CContractFilePage();
			cContractFilePage.setFileId(sourceFileId);
			cContractFilePage.setPageNo(i);
			cContractFilePage.setPagePath((String) fileInfo.get("location"));
			cContractFilePage.setCreateTime(new Date());
			cContractFilePage.setUpdateTime(new Date());
			comparePageList.add(cContractFilePage);

			Map<String, Object> fileObj = new HashMap<>();
			// 文件访问地址
			fileObj.put("filePath", fileInfo.get("filePath"));
			// 文件对象
			fileObj.put("file", cContractFilePage);
			resultList.add(fileObj);
		}
		cContractFilePageDao.insertBatch(comparePageList);
		log.info(JSONArray.toJSONString(comparePageList));
		return resultList;
	}

	@Override
	@Async
	public void doCompare(Integer recordId, File sourceFilePath, Integer fileId) throws Exception {
		log.info("4");

		// 2、文档转图片
		List<Map<String, Object>> sourceFilePagePathList = saveFilePage(sourceFilePath, fileId);
		log.info(sourceFilePagePathList.toString());
		// 3、OCR识别文档
		List<CContractFilePage> sourceFileResultList = new ArrayList<>();
		for (Map<String, Object> filePage : sourceFilePagePathList) {
			CContractFilePage pageInfo = (CContractFilePage) filePage.get("file");
			String filePath = (String) filePage.get("filePath");
			String sourceFileResult = HttpUtils.getResp(filePath);
			pageInfo.setCompareResult(sourceFileResult);
			pageInfo.setContractId(recordId);
			pageInfo.setPagePath(pageInfo.getPagePath());
			sourceFileResultList.add(pageInfo);
		}
		cContractFilePageDao.insertOrUpdateBatch(sourceFileResultList);
	}

	@Override
	public Integer saveFilePath(MultipartFile file, String filePath) {
		CContractFile compareFileEntity = new CContractFile();
		compareFileEntity.setFileName(file.getOriginalFilename());
		compareFileEntity.setFilePath(filePath);
		compareFileEntity.setFileType(file.getContentType());
		compareFileEntity.setCreateTime(new Date());
		cContractFileDao.insertOne(compareFileEntity);
		return compareFileEntity.getId();
	}

}

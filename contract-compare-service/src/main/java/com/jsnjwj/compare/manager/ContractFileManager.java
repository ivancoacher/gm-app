package com.jsnjwj.compare.manager;

import com.alibaba.fastjson2.JSONArray;
import com.jsnjwj.compare.dao.CContractFileDao;
import com.jsnjwj.compare.dao.CContractFilePageDao;
import com.jsnjwj.compare.dao.CContractRecordDao;
import com.jsnjwj.compare.entity.CContractFileEntity;
import com.jsnjwj.compare.entity.CContractFilePageEntity;
import com.jsnjwj.compare.entity.CContractRecordEntity;
import com.jsnjwj.compare.enums.CompareStateEnum;
import com.jsnjwj.compare.utils.FileUtils;
import com.jsnjwj.compare.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class ContractFileManager {

	@Resource
	private CContractFilePageDao cContractFilePageDao;

	@Resource
	private CContractFileDao cContractFileDao;

	private List<Map<String, Object>> saveFilePage(File compareFilePath, Integer sourceFileId) throws Exception {
		List<Map<String, Object>> compareFilePagePathList = FileUtils.pdf2Image(compareFilePath);
		List<CContractFilePageEntity> comparePageList = new ArrayList<>();
		List<Map<String, Object>> resultList = new ArrayList<>();
		for (int i = 1; i <= compareFilePagePathList.size(); i++) {
			Map<String, Object> fileInfo = compareFilePagePathList.get(i - 1);

			CContractFilePageEntity cContractFilePageEntity = new CContractFilePageEntity();
			cContractFilePageEntity.setFileId(sourceFileId);
			cContractFilePageEntity.setPageNo(i);
			cContractFilePageEntity.setPagePath((String) fileInfo.get("location"));
			cContractFilePageEntity.setCreateTime(new Date());
			cContractFilePageEntity.setUpdateTime(new Date());
			comparePageList.add(cContractFilePageEntity);

			Map<String, Object> fileObj = new HashMap<>();
			// 文件访问地址
			fileObj.put("filePath", fileInfo.get("filePath"));
			// 文件对象
			fileObj.put("file", cContractFilePageEntity);
			resultList.add(fileObj);
		}
		cContractFilePageDao.insertBatch(comparePageList);
		log.info(JSONArray.toJSONString(comparePageList));
		return resultList;
	}

	@Async
	public void doCompare(Integer recordId, File sourceFilePath, Integer fileId) throws Exception {
		log.info("4");

		// 2、文档转图片
		List<Map<String, Object>> sourceFilePagePathList = saveFilePage(sourceFilePath, fileId);
		log.info(sourceFilePagePathList.toString());
		// 3、OCR识别文档
		List<CContractFilePageEntity> sourceFileResultList = new ArrayList<>();
		for (Map<String, Object> filePage : sourceFilePagePathList) {
			CContractFilePageEntity pageInfo = (CContractFilePageEntity) filePage.get("file");
			String filePath = (String) filePage.get("filePath");
			String sourceFileResult = HttpUtils.getResp(filePath);
			pageInfo.setCompareResult(sourceFileResult);
			pageInfo.setContractId(recordId);
			pageInfo.setPagePath(pageInfo.getPagePath());
			sourceFileResultList.add(pageInfo);
		}
		cContractFilePageDao.insertOrUpdateBatch(sourceFileResultList);
	}

	public Integer saveFilePath(MultipartFile file, String filePath) {
		CContractFileEntity compareFileEntity = new CContractFileEntity();
		compareFileEntity.setFileName(file.getOriginalFilename());
		compareFileEntity.setFilePath(filePath);
		compareFileEntity.setFileType(file.getContentType());
		compareFileEntity.setCreateTime(new Date());
		cContractFileDao.insertOne(compareFileEntity);
		return compareFileEntity.getId();
	}

}

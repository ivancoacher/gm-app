package com.jsnjwj.compare.manager;

import com.jsnjwj.compare.dao.CContractRecordDao;
import com.jsnjwj.compare.entity.CContractRecordEntity;
import com.jsnjwj.compare.enums.CompareStateEnum;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class ContractManager {

	@Resource
	private CContractRecordDao contractRecordDao;

	public void initRecord(CContractRecordEntity recordEntity) {
		// 格式化当天日期
		LocalDateTime dateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		// 补充其他初始数据
		recordEntity.setCompareState(CompareStateEnum.HANDLING.getCode());
		recordEntity.setOriginFileId(0L);
		recordEntity.setCompareFileId(0L);
		recordEntity.setCreateTime(dateTime);
		recordEntity.setUpdateTime(dateTime);
		// 年-月-日
		recordEntity.setOperateDay(dateTime.format(formatter));
		contractRecordDao.insert(recordEntity);
	}

	public void changeCompareState(CContractRecordEntity recordEntity) {
		contractRecordDao.updateById(recordEntity);
	}

}

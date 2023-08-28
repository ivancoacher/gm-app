package com.jsnjwj.facade.manager;

import com.jsnjwj.facade.entity.SignArrangeRecordEntity;
import com.jsnjwj.facade.mapper.SignArrangeRecordMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 配置信息-报名数据
 */
@Service
public class GameArrangeRecordManager {

    @Resource
    private SignArrangeRecordMapper tcsSignArrangeRecordMapper;

    private Integer updateRecord(SignArrangeRecordEntity record) {
        return tcsSignArrangeRecordMapper.updateById(record);
    }

    private Integer saveBatchRecord(List<SignArrangeRecordEntity> list) {
        return tcsSignArrangeRecordMapper.insertBatchSomeColumn(list);
    }

}

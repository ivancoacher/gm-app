package com.jsnjwj.facade.manager;

import com.jsnjwj.facade.entity.TcSignArrangeRecord;
import com.jsnjwj.facade.mapper.TcSignArrangeRecordMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 配置信息-报名数据
 */
@Service
public class GameArrangeRecordManager {

    @Resource
    private TcSignArrangeRecordMapper tcsSignArrangeRecordMapper;

    private Integer updateRecord(TcSignArrangeRecord record) {
        return tcsSignArrangeRecordMapper.updateById(record);
    }

    private Integer saveBatchRecord(List<TcSignArrangeRecord> list) {
        return tcsSignArrangeRecordMapper.insertBatchSomeColumn(list);
    }

}

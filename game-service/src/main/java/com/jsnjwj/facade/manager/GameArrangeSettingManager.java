package com.jsnjwj.facade.manager;

import com.jsnjwj.facade.entity.TcSignArrangeSetting;
import com.jsnjwj.facade.entity.TcSignArrangeSettingDetail;
import com.jsnjwj.facade.mapper.TcSignArrangeSettingDetailMapper;
import com.jsnjwj.facade.mapper.TcSignArrangeSettingMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 配置信息-报名数据
 */
@Service
public class GameArrangeSettingManager {

    @Resource
    private TcSignArrangeSettingDetailMapper signArrangeSettingDetailMapper;

    @Resource
    private TcSignArrangeSettingMapper signArrangeSettingMapper;

    private Integer updateSetting(TcSignArrangeSetting record) {
        return signArrangeSettingMapper.updateById(record);
    }

    private Integer syncSettingDetail(Long settingId, List<TcSignArrangeSettingDetail> list) {
        return signArrangeSettingDetailMapper.insertBatchSomeColumn(list);
    }

}

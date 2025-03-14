package com.jsnjwj.facade.manager;

import com.jsnjwj.facade.entity.SignArrangeSettingDetailEntity;
import com.jsnjwj.facade.entity.SignArrangeSettingEntity;
import com.jsnjwj.facade.mapper.SignArrangeSettingDetailMapper;
import com.jsnjwj.facade.mapper.SignArrangeSettingMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 配置信息-报名数据
 */
@Service
public class GameArrangeSettingManager {

	@Resource
	private SignArrangeSettingDetailMapper signArrangeSettingDetailMapper;

	@Resource
	private SignArrangeSettingMapper signArrangeSettingMapper;

	private Integer updateSetting(SignArrangeSettingEntity record) {
		return signArrangeSettingMapper.updateById(record);
	}

	private Integer syncSettingDetail(Long settingId, List<SignArrangeSettingDetailEntity> list) {
		return signArrangeSettingDetailMapper.insertBatchSomeColumn(list);
	}

}

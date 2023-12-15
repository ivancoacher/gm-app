package com.jsnjwj.facade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsnjwj.facade.entity.GameItemEntity;
import com.jsnjwj.facade.entity.SignArrangeRecordEntity;
import com.jsnjwj.facade.entity.SignArrangeSequenceEntity;
import com.jsnjwj.facade.mapper.SignArrangeRecordMapper;
import com.jsnjwj.facade.mapper.SignArrangeSequenceMapper;
import com.jsnjwj.facade.service.SignArrangeSequenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class SignArrangeSequenceServiceImpl implements SignArrangeSequenceService {
    private final SignArrangeRecordMapper signArrangeRecordMapper;

    private final SignArrangeSequenceMapper signArrangeSequenceMapper;

    @Override
    public void syncArrangeRecord() {

    }

    /**
     * 自动同步arrange_record数据
     */
    @Async
    @Override
    public void syncArrangeRecord(GameItemEntity itemEntity) {
        Long gameId = itemEntity.getGameId();
        Long itemId = itemEntity.getId();
        LambdaQueryWrapper<SignArrangeRecordEntity> query = new LambdaQueryWrapper<>();
        query.eq(SignArrangeRecordEntity::getGameId, gameId);
        query.eq(SignArrangeRecordEntity::getItemId, itemId);
        query.last("limit 1");
        SignArrangeRecordEntity arrangeRecord = signArrangeRecordMapper.selectOne(query);
        if (Objects.isNull(arrangeRecord)) {
            arrangeRecord = new SignArrangeRecordEntity();
            arrangeRecord.setGameId(gameId);
            arrangeRecord.setGroupId(itemEntity.getGroupId());
            arrangeRecord.setItemId(itemId);
            arrangeRecord.setCreatedAt(new Date());
            signArrangeRecordMapper.insert(arrangeRecord);
        } else {
            arrangeRecord.setGroupId(itemEntity.getGroupId());
            arrangeRecord.setUpdatedAt(new Date());
            signArrangeRecordMapper.updateById(arrangeRecord);
        }
    }

    /**
     * 选手分组信息列表
     *
     * @param gameId
     * @return
     */
    @Override
    public List<?> fetchList(Long gameId) {
        LambdaQueryWrapper<SignArrangeRecordEntity> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(SignArrangeRecordEntity::getGameId, gameId);
        wrapper.orderByAsc(SignArrangeRecordEntity::getRecordName);
        List<SignArrangeRecordEntity> records = signArrangeRecordMapper.selectList(wrapper);

        List<Long> recordIds = records.stream().map(SignArrangeRecordEntity::getId).collect(Collectors.toList());

        LambdaQueryWrapper<SignArrangeSequenceEntity> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(SignArrangeSequenceEntity::getGameId, gameId);
        wrapper1.orderByAsc(SignArrangeSequenceEntity::getSort);
        List<SignArrangeSequenceEntity> sequenceEntityList = signArrangeSequenceMapper.selectList(wrapper1);


        return new ArrayList<>();


    }

    /**
     * 更新选手出场殊勋
     *
     * @return
     */
    @Override
    public int update() {
        return 1;
    }

}

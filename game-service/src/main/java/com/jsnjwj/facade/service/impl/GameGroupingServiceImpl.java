package com.jsnjwj.facade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.facade.dto.GroupingItemDto;
import com.jsnjwj.facade.entity.TcGameItem;
import com.jsnjwj.facade.entity.TcGameRuleSetting;
import com.jsnjwj.facade.mapper.TcGameItemMapper;
import com.jsnjwj.facade.mapper.TcGameRuleSettingMapper;
import com.jsnjwj.facade.query.GameGroupingViewQuery;
import com.jsnjwj.facade.service.GameGroupingService;
import com.jsnjwj.facade.vo.ItemLabelVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 合同比对service
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class GameGroupingServiceImpl implements GameGroupingService {

    private final TcGameItemMapper tcGameItemMapper;

    private final TcGameRuleSettingMapper tcGameRuleSettingMapper;
    @Override
    public Page<GroupingItemDto> fetchGroupingItem(GameGroupingViewQuery query) {

        Page<TcGameItem> page = new Page<>(query.getPage(), query.getPageSize());

        LambdaQueryWrapper<TcGameItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Objects.nonNull(query.getGroupId()), TcGameItem::getGroupId, query.getGroupId());
        wrapper.eq(TcGameItem::getGameId, query.getGameId());

        wrapper.orderByAsc(TcGameItem::getSort);

        Page<ItemLabelVo> rst = tcGameItemMapper.selectByPage(page, wrapper);

        Page<GroupingItemDto> response = new Page<>();

        response.setTotal(rst.getTotal());
        response.setCurrent(rst.getCurrent());
        response.setPages(rst.getPages());

        List<GroupingItemDto> records = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(rst.getRecords())){

            List<Long> itemIds = rst.getRecords().stream().map(ItemLabelVo::getItemId).collect(Collectors.toList());
            Map<Long,TcGameRuleSetting> ruleSets = getRuleSettingMap(itemIds);
            rst.getRecords().forEach(record->{
                GroupingItemDto groupingItemDto = new GroupingItemDto();

                groupingItemDto.setGameId(query.getGameId());
                groupingItemDto.setGroupId(query.getGroupId());
                groupingItemDto.setItemId(query.getItemId());
                groupingItemDto.setGroupName(record.getGroupName());
                groupingItemDto.setItemName(record.getItemName());
                if (Objects.nonNull(ruleSets.get(record.getItemId()))){
                    groupingItemDto.setRule(ruleSets.get(record.getItemId()).getScoreRule().toString());
                }
                records.add(groupingItemDto);
            });
        }
        response.setRecords(records);
        return response;

    }

    private Map<Long, TcGameRuleSetting> getRuleSettingMap(List<Long> itemIdList){
        LambdaQueryWrapper<TcGameRuleSetting> query = new LambdaQueryWrapper<>();
        query.in(TcGameRuleSetting::getItemId,itemIdList);
        List<TcGameRuleSetting> result = tcGameRuleSettingMapper.selectList(query);

        return result.stream().collect(Collectors.toMap(TcGameRuleSetting::getItemId, Function.identity()));
    }
}

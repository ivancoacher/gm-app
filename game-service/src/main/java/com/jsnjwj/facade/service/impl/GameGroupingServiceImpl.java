package com.jsnjwj.facade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.facade.dto.GroupingDetailDto;
import com.jsnjwj.facade.dto.GroupingItemDto;
import com.jsnjwj.facade.entity.GameItemEntity;
import com.jsnjwj.facade.entity.SignArrangeRecordEntity;
import com.jsnjwj.facade.entity.SignSingleEntity;
import com.jsnjwj.facade.entity.TcGameRuleSetting;
import com.jsnjwj.facade.mapper.TcGameItemMapper;
import com.jsnjwj.facade.mapper.TcGameRuleSettingMapper;
import com.jsnjwj.facade.mapper.TcSignSingleMapper;
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

    private final TcSignSingleMapper tcSignSingleMapper;

    /**
     * 查询 分组列表
     *
     * @param query
     * @return
     */
    @Override
    public Page<GroupingItemDto> fetchGroupingItem(GameGroupingViewQuery query) {

        Page<SignArrangeRecordEntity> page = new Page<>(query.getPage(), query.getPageSize());

        LambdaQueryWrapper<SignArrangeRecordEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Objects.nonNull(query.getGroupId()), SignArrangeRecordEntity::getGroupId, query.getGroupId());
        wrapper.eq(SignArrangeRecordEntity::getGameId, query.getGameId());

        Page<ItemLabelVo> rst = tcGameItemMapper.selectByPage(page, wrapper);

        Page<GroupingItemDto> response = new Page<>();

        response.setTotal(rst.getTotal());
        response.setCurrent(rst.getCurrent());
        response.setPages(rst.getPages());

        List<GroupingItemDto> records = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(rst.getRecords())) {

            List<Long> itemIds = rst.getRecords().stream().map(ItemLabelVo::getItemId).collect(Collectors.toList());
            Map<Long, TcGameRuleSetting> ruleSets = getRuleSettingMap(itemIds);
            rst.getRecords().forEach(record -> {
                GroupingItemDto groupingItemDto = new GroupingItemDto();

                groupingItemDto.setGameId(record.getGameId());
                groupingItemDto.setGroupId(record.getGroupId());
                groupingItemDto.setItemId(record.getItemId());
                groupingItemDto.setGroupName(record.getGroupName());
                groupingItemDto.setItemName(record.getItemName());
                if (Objects.nonNull(ruleSets.get(record.getItemId()))) {
                    groupingItemDto.setJudgeCount(ruleSets.get(record.getItemId()).getJudgeGroupNum());
                    groupingItemDto.setRule(ruleSets.get(record.getItemId()).getScoreRule().toString());
                }
                records.add(groupingItemDto);
            });
        }
        response.setRecords(records);
        return response;

    }

    private Map<Long, TcGameRuleSetting> getRuleSettingMap(List<Long> itemIdList) {
        LambdaQueryWrapper<TcGameRuleSetting> query = new LambdaQueryWrapper<>();
        query.in(TcGameRuleSetting::getItemId, itemIdList);
        List<TcGameRuleSetting> result = tcGameRuleSettingMapper.selectList(query);

        return result.stream().collect(Collectors.toMap(TcGameRuleSetting::getItemId, Function.identity()));
    }

    @Override
    public GroupingDetailDto fetchGroupingDetail(GameGroupingViewQuery query) {

        LambdaQueryWrapper<GameItemEntity> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(GameItemEntity::getGameId, query.getGameId());

        wrapper.orderByAsc(GameItemEntity::getSort);

        List<GameItemEntity> itemList = tcGameItemMapper.selectList(wrapper);

        GroupingDetailDto response = new GroupingDetailDto();
        response.setGameId(query.getGameId());
        if (CollectionUtils.isEmpty(itemList)) {
            return response;
        }

        List<GroupingDetailDto.GroupingItem> items = new ArrayList<>();

        for (GameItemEntity item : itemList) {
            Long itemId = item.getId();
            Long gameId = item.getGameId();

            // 配置item信息
            GroupingDetailDto.GroupingItem itemDtos = new GroupingDetailDto.GroupingItem();

            itemDtos.setItemName(item.getItemName());
            itemDtos.setItemId(itemId);

            // 查询该item下的报名信息
            LambdaQueryWrapper<SignSingleEntity> wrapper1 = new LambdaQueryWrapper<>();

            wrapper1.eq(SignSingleEntity::getGameId, query.getGameId());
            wrapper1.eq(SignSingleEntity::getItemId, itemId);
            List<SignSingleEntity> signSingles = tcSignSingleMapper.selectList(wrapper1);
            List<GroupingDetailDto.GroupingItemSign> groupingItemSigns = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(signSingles)) {

                for (SignSingleEntity signSingle : signSingles) {
                    GroupingDetailDto.GroupingItemSign sign = new GroupingDetailDto.GroupingItemSign();
                    sign.setName(signSingle.getName());
                    sign.setTeam(signSingle.getTeamName());
                    groupingItemSigns.add(sign);
                }

            }

            itemDtos.setGroupItemSignList(groupingItemSigns);
            items.add(itemDtos);

        }
        response.setGroupItemList(items);
        return response;
    }


    /**
     * 修改分组信息
     */
    @Override
    public int updateArrangeRecord() {
        return 0;
    }

    /**
     * 修改分组
     */

    @Override
    public int updateArrangeGrouping() {
        return 0;
    }

    /**
     * 生成场序表
     */
    @Override
    public int initArrangeOrder(){
        return 0;
    }

}

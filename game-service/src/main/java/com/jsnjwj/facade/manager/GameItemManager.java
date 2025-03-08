package com.jsnjwj.facade.manager;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.entity.GameItemEntity;
import com.jsnjwj.facade.mapper.GameItemMapper;
import com.jsnjwj.facade.query.GameItemListQuery;
import com.jsnjwj.facade.vo.ItemLabelVo;
import com.jsnjwj.facade.vo.rule.GameItemRuleVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameItemManager {

    private final GameItemMapper gameItemMapper;

    public List<ItemLabelVo> fetchItems(Long gameId) {
        List<ItemLabelVo> response = new ArrayList<>();
        return response;
    }

    public Page<ItemLabelVo> fetchItemsPage(GameItemListQuery query) {
        Long gameId = query.getGameId();
        Page<GameItemEntity> page = new Page<>(query.getPage(), query.getLimit());
        LambdaQueryWrapper<GameItemEntity> lambdaQuery = new LambdaQueryWrapper<>();
        lambdaQuery.eq(GameItemEntity::getGameId, gameId);

        lambdaQuery.eq(!StringUtils.isEmpty(query.getGroupId()), GameItemEntity::getGroupId, query.getGroupId());
        lambdaQuery.orderByAsc(GameItemEntity::getSort);
        return gameItemMapper.selectByPage(page, lambdaQuery);
    }

    /**
     * 获取项目列表
     *
     * @param query
     * @return
     */
    public List<GameItemEntity> fetchList(GameItemListQuery query) {
        LambdaQueryWrapper<GameItemEntity> lambdaQuery = new LambdaQueryWrapper<>();
        lambdaQuery.eq(Objects.nonNull(query.getGameId()), GameItemEntity::getGameId, query.getGameId());
        lambdaQuery.eq(Objects.nonNull(query.getGroupId()), GameItemEntity::getGroupId, query.getGroupId());
        return gameItemMapper.selectList(lambdaQuery);
    }

    public Map<Long, GameItemEntity> fetchItemMap(List<Long> itemIds) {
        LambdaQueryWrapper<GameItemEntity> lambdaQuery = new LambdaQueryWrapper<>();
        lambdaQuery.in(GameItemEntity::getId, itemIds);
        List<GameItemEntity> result = gameItemMapper.selectList(lambdaQuery);
        return CollectionUtil.isNotEmpty(result) ? result.stream().collect(Collectors.toMap(GameItemEntity::getId, item -> item)) : new HashMap<>();
    }



    public List<ItemLabelVo> fetchItemsByGroupId(Long groupId) {
        List<ItemLabelVo> response = new ArrayList<>();
        return response;
    }

    public int save(GameItemEntity gameItemEntity) {
        return gameItemMapper.insert(gameItemEntity);
    }

    public int update(GameItemEntity gameItemEntity) {
        return gameItemMapper.updateById(gameItemEntity);
    }

    public int delete(Long itemId) {
        return gameItemMapper.deleteById(itemId);
    }

    public GameItemEntity fetchItemInfo(Long itemId) {
        return gameItemMapper.selectById(itemId);
    }


}

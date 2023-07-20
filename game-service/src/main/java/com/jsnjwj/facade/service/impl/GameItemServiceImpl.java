package com.jsnjwj.facade.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.entity.TcGameGroup;
import com.jsnjwj.facade.entity.TcGameItem;
import com.jsnjwj.facade.manager.GameGroupManager;
import com.jsnjwj.facade.manager.GameItemManager;
import com.jsnjwj.facade.query.GameItemListQuery;
import com.jsnjwj.facade.query.GameItemSaveQuery;
import com.jsnjwj.facade.query.GameItemUpdateQuery;
import com.jsnjwj.facade.service.GameItemService;
import com.jsnjwj.facade.vo.GameItemVo;
import com.jsnjwj.facade.vo.ItemLabelVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service
public class GameItemServiceImpl implements GameItemService {
    @Resource
    private GameItemManager gameItemManager;
    /**
     * 分页查询
     *
     * @return
     */
    @Override
    public ApiResponse<Page<ItemLabelVo>> fetchPages(GameItemListQuery query) {

        return gameItemManager.fetchItemsPage(query);
    }

    /**
     * 查询全部
     *
     * @return
     */
    @Override
    public List<ItemLabelVo> fetchList(Long gameId, Long groupId) {
        return Collections.emptyList();
    }

    @Override
    public void importData() {

    }

    @Override
    public int save(GameItemSaveQuery query) {
        TcGameItem tcGameGroup = new TcGameItem();
        tcGameGroup.setGameId(query.getGameId());
        tcGameGroup.setGroupId(query.getGroupId());
        tcGameGroup.setItemName(query.getItemName());
        tcGameGroup.setSort(query.getSort());
        return gameItemManager.save(tcGameGroup);
    }
    @Override
    public int update(GameItemUpdateQuery query) {
        TcGameItem tcGameGroup = new TcGameItem();
        tcGameGroup.setGroupId(query.getGroupId());
        tcGameGroup.setSort(query.getSort());
        tcGameGroup.setId(query.getItemId());
        tcGameGroup.setItemName(query.getItemName());
        return gameItemManager.update(tcGameGroup);
    }

}

package com.jsnjwj.facade.manager;

import com.jsnjwj.facade.entity.TcGameItem;
import com.jsnjwj.facade.mapper.TcGameItemMapper;
import com.jsnjwj.facade.vo.ItemLabelVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameItemManager {
    private final TcGameItemMapper gameItemMapper;
    public List<ItemLabelVo> fetchItems(Long gameId) {
        List<ItemLabelVo> response = new ArrayList<>();
        return response;
    }

    public List<ItemLabelVo> fetchItemsPage(Long gameId) {
        List<ItemLabelVo> response = new ArrayList<>();
        return response;
    }

    public List<ItemLabelVo> fetchItemsByGroupId(Long groupId) {
        List<ItemLabelVo> response = new ArrayList<>();
        return response;
    }

    public int saveItem(TcGameItem tcGameItem) {
        return gameItemMapper.insert(tcGameItem);
    }

    public int updateItem(TcGameItem tcGameItem) {
        return gameItemMapper.updateById(tcGameItem);
    }

    public int deleteItem(Long itemId) {
        return gameItemMapper.deleteById(itemId);
    }

}

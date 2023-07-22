package com.jsnjwj.facade.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.query.GameItemListQuery;
import com.jsnjwj.facade.query.GameItemSaveQuery;
import com.jsnjwj.facade.query.GameItemUpdateQuery;
import com.jsnjwj.facade.vo.GameItemVo;
import com.jsnjwj.facade.vo.ItemLabelVo;

import java.util.List;

public interface GameItemService {

	ApiResponse<Page<ItemLabelVo>> fetchPages(GameItemListQuery query);

	List<ItemLabelVo> fetchList(GameItemListQuery query);

	void importData();

	int save(GameItemSaveQuery query);

	int update(GameItemUpdateQuery query);

	ApiResponse<Integer> delete(GameItemUpdateQuery query);

}

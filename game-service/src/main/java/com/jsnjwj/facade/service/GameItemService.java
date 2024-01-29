package com.jsnjwj.facade.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.request.BaseRequest;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.entity.GameItemEntity;
import com.jsnjwj.facade.query.GameItemListQuery;
import com.jsnjwj.facade.query.GameItemSaveQuery;
import com.jsnjwj.facade.query.GameItemUpdateQuery;
import com.jsnjwj.facade.vo.ItemLabelVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GameItemService {

	ApiResponse<Page<ItemLabelVo>> fetchPages(GameItemListQuery query);

	List<ItemLabelVo> fetchList(GameItemListQuery query);

	void importData(BaseRequest query, MultipartFile file);

	int save(GameItemSaveQuery query);

	int update(GameItemUpdateQuery query);

	GameItemEntity fetchOne(Long itemId);

	ApiResponse<Integer> delete(GameItemUpdateQuery query);

}

package com.jsnjwj.facade.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.request.BaseRequest;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.query.GameGroupListQuery;
import com.jsnjwj.facade.query.GameGroupSaveQuery;
import com.jsnjwj.facade.query.GameGroupUpdateQuery;
import com.jsnjwj.facade.vo.GameGroupAllVo;
import com.jsnjwj.facade.vo.GroupLabelVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GameGroupService {

	Page<GroupLabelVo> fetchPages(GameGroupListQuery query);

	List<GroupLabelVo> fetchList(GameGroupListQuery query);

	List<GameGroupAllVo> fetchAll(Long gameId);

	ApiResponse<?> save(GameGroupSaveQuery query);

	ApiResponse<?> importData(BaseRequest request, MultipartFile file);

	ApiResponse<?> update(GameGroupUpdateQuery query);

	ApiResponse<?> delete(Long groupId);

}

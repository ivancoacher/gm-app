package com.jsnjwj.facade.query;

import com.jsnjwj.common.request.BaseRequest;
import com.jsnjwj.facade.vo.GroupLabelVo;
import lombok.Data;

import java.util.List;

@Data
public class GameGroupBatchUpdateQuery extends BaseRequest {

	List<GroupLabelVo> data;

}

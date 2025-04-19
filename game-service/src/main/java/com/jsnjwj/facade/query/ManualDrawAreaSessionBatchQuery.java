package com.jsnjwj.facade.query;

import com.jsnjwj.common.request.BaseRequest;
import com.jsnjwj.facade.query.session.ManualDrawQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ManualDrawAreaSessionBatchQuery extends BaseRequest {

	List<ManualDrawAreaSessionQuery> data;

	private Long areaId;

}

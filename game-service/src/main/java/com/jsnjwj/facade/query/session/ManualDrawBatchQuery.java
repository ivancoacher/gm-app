package com.jsnjwj.facade.query.session;

import com.jsnjwj.common.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ManualDrawBatchQuery extends BaseRequest {

	List<ManualDrawQuery> data;

}

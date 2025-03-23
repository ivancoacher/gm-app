package com.jsnjwj.facade.query;

import com.jsnjwj.common.request.BaseRequest;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class SignSingleImportQuery extends BaseRequest {

	private Integer importType;

	private MultipartFile file;

}

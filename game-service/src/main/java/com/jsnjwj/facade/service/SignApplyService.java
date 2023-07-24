package com.jsnjwj.facade.service;

import com.jsnjwj.common.request.BaseRequest;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.query.SignSingleListQuery;
import com.jsnjwj.facade.query.SignTeamListQuery;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface SignApplyService {

	ApiResponse<?> fetchSinglePage(SignSingleListQuery query);

	ApiResponse<?> fetchTeamPage(SignTeamListQuery query);

	ApiResponse<?> importTeam(BaseRequest baseRequest, MultipartFile file) throws IOException;

	ApiResponse<?> importSingle(BaseRequest baseRequest, MultipartFile file);

}

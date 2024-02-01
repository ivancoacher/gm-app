package com.jsnjwj.facade.service;

import com.jsnjwj.common.request.BaseRequest;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.query.SignSingleImportQuery;
import com.jsnjwj.facade.query.SignSingleListQuery;
import com.jsnjwj.facade.query.SignSingleProgramExportQuery;
import com.jsnjwj.facade.query.SignTeamListQuery;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface SignApplyService {

	ApiResponse<?> fetchSinglePage(SignSingleListQuery query);

	ApiResponse<?> fetchTeamPage(SignTeamListQuery query);

	ApiResponse<?> fetchTeamData(SignTeamListQuery query);

	ApiResponse<?> importTeam(BaseRequest baseRequest, MultipartFile file) throws IOException;

	ApiResponse<?> exportTeamDemo(BaseRequest baseRequest, MultipartFile file) throws IOException;

	ApiResponse<?> importSingle(Integer importType, MultipartFile file);

	ApiResponse<?> exportSingleDemo(BaseRequest baseRequest, MultipartFile file);

}

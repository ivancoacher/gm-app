package com.jsnjwj.facade.service;

import com.jsnjwj.common.request.BaseRequest;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.query.SignSingleListQuery;
import com.jsnjwj.facade.query.SignSingleUpdateQuery;
import com.jsnjwj.facade.query.SignTeamListQuery;
import com.jsnjwj.facade.query.SignTeamUpdateQuery;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface SignApplyService {

	ApiResponse<?> fetchSinglePage(SignSingleListQuery query);

	ApiResponse<?> fetchTeamPage(SignTeamListQuery query);

	ApiResponse<?> fetchTeamData(SignTeamListQuery query);

	ApiResponse<?> updateTeam(SignTeamUpdateQuery query);

	ApiResponse<?> deleteTeam(Long teamId);

	ApiResponse<?> fetchTeam(Long teamId);

	ApiResponse<?> importTeam(BaseRequest baseRequest, MultipartFile file) throws IOException;

	ApiResponse<?> exportTeamDemo(BaseRequest baseRequest, MultipartFile file) throws IOException;

	ApiResponse<?> importSingle(Integer importType, MultipartFile file);

	void importExample(HttpServletResponse response) throws IOException;

	ApiResponse<?> exportSingleDemo(BaseRequest baseRequest, MultipartFile file);

	ApiResponse<?> signDelete(Long signId);

	ApiResponse<?> signDeleteBatch(List<Long> signIds);

	ApiResponse<?> signUpdate(SignSingleUpdateQuery request);

	ApiResponse<?> getOrgList(Long gameId);

}

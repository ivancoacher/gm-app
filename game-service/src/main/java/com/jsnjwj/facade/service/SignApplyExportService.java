package com.jsnjwj.facade.service;

import com.jsnjwj.common.request.BaseRequest;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.query.SignSingleListQuery;
import com.jsnjwj.facade.query.SignSingleProgramExportQuery;
import com.jsnjwj.facade.query.SignTeamListQuery;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface SignApplyExportService {

	ApiResponse<?> exportSignProgram(SignSingleProgramExportQuery request);

}

package com.jsnjwj.facade.service;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.query.SignSingleProgramExportQuery;

public interface SignApplyExportService {

	ApiResponse<?> exportSignProgram(SignSingleProgramExportQuery request);

	ApiResponse<?> exportSignProjectProgram(SignSingleProgramExportQuery request);

}

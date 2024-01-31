package com.jsnjwj.api.controller;

import com.alibaba.fastjson2.JSON;
import com.jsnjwj.common.request.BaseRequest;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.common.utils.ThreadLocalUtil;
import com.jsnjwj.facade.query.SignSingleImportQuery;
import com.jsnjwj.facade.query.SignSingleListQuery;
import com.jsnjwj.facade.query.SignSingleProgramExportQuery;
import com.jsnjwj.facade.query.SignTeamListQuery;
import com.jsnjwj.facade.service.SignApplyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/game/sign")
public class GameSignController {

	@Resource
	private SignApplyService signApplyService;

	@RequestMapping("/single/page")
	public ApiResponse<?> fetchSinglePage(SignSingleListQuery query) {
		query.setGameId(ThreadLocalUtil.getCurrentGameId());
		query.setUserId(ThreadLocalUtil.getCurrentUserId());
		return signApplyService.fetchSinglePage(query);
	}

	@RequestMapping("/single/import")
	public ApiResponse<?> singleImport(@RequestParam("importType") Integer importType,
			@RequestParam("file") MultipartFile sourceFile) {
		return signApplyService.importSingle(importType, sourceFile);
	}

	@RequestMapping("/single/program/export")
	public ApiResponse<?> singleImport(@RequestBody SignSingleProgramExportQuery request) {
		request.setGameId(ThreadLocalUtil.getCurrentGameId());
		return signApplyService.exportSignProgram(request);
	}

	@RequestMapping("/single/demo/import")
	public ApiResponse<?> singleDemoImport(BaseRequest query, MultipartFile sourceFile) throws Exception {
		query.setUserId(ThreadLocalUtil.getCurrentUserId());
		return signApplyService.exportSingleDemo(query, sourceFile);
	}

	@RequestMapping("/team/page")
	public ApiResponse<?> fetchTeamPage(SignTeamListQuery query) {
		query.setGameId(ThreadLocalUtil.getCurrentGameId());
		query.setUserId(ThreadLocalUtil.getCurrentUserId());
		return signApplyService.fetchTeamPage(query);
	}

	@RequestMapping("/team/data")
	public ApiResponse<?> fetchTeamData(SignTeamListQuery query) {
		query.setGameId(ThreadLocalUtil.getCurrentGameId());
		return signApplyService.fetchTeamData(query);
	}

	@RequestMapping("/team/import")
	public ApiResponse<?> teamImport(BaseRequest query, HttpServletRequest request) throws Exception {
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
		MultipartFile sourceFile = multiRequest.getFile("file");
		query.setUserId(ThreadLocalUtil.getCurrentUserId());
		return signApplyService.importTeam(query, sourceFile);
	}

	@RequestMapping("/team/demo/export")
	public ApiResponse<?> teamDemoExport(BaseRequest query, HttpServletRequest request) throws Exception {
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
		MultipartFile sourceFile = multiRequest.getFile("file");
		query.setUserId(ThreadLocalUtil.getCurrentUserId());
		return signApplyService.exportTeamDemo(query, sourceFile);
	}

}

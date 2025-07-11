package com.jsnjwj.api.controller;

import com.jsnjwj.common.request.BaseRequest;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.common.utils.ThreadLocalUtil;
import com.jsnjwj.facade.query.*;
import com.jsnjwj.facade.service.SignApplyExportService;
import com.jsnjwj.facade.service.SignApplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/game/sign")
@RequiredArgsConstructor
public class GameSignController {

	private final SignApplyService signApplyService;

	private final SignApplyExportService signApplyExportService;

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

	@GetMapping("/single/importExample")
	public void importExample(HttpServletResponse response) throws IOException {
		signApplyService.importExample(response);
	}

	/**
	 * 秩序册导出
	 * @param request SignSingleProgramExportQuery
	 * @return ApiResponse
	 */
	@RequestMapping("/single/program/export")
	public ApiResponse<?> singleImport(@RequestBody SignSingleProgramExportQuery request) {
		request.setGameId(ThreadLocalUtil.getCurrentGameId());
		return signApplyExportService.exportSignProgram(request);
	}

	@RequestMapping("/project/program/export")
	public ApiResponse<?> projectImport(@RequestBody SignSingleProgramExportQuery request) {
		request.setGameId(ThreadLocalUtil.getCurrentGameId());
		return signApplyExportService.exportSignProjectProgram(request);
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

	@RequestMapping("/team/update")
	public ApiResponse<?> updateTeam(@RequestBody SignTeamUpdateQuery query) {
		query.setGameId(ThreadLocalUtil.getCurrentGameId());
		return signApplyService.updateTeam(query);
	}

	@DeleteMapping("/team/{teamId}")
	public ApiResponse<?> deleteTeam(@PathVariable Long teamId) {

		return signApplyService.deleteTeam(teamId);
	}

	@DeleteMapping("/single/{signId}")
	public ApiResponse<?> deleteSign(@PathVariable Long signId) {

		return signApplyService.signDelete(signId);
	}

	@PostMapping("/single")
	public ApiResponse<?> deleteSigns(@RequestBody List<Long> signIds) {

		return signApplyService.signDeleteBatch(signIds);
	}

	@RequestMapping("/team/info/{id}")
	public ApiResponse<?> fetchTeam(@PathVariable Long id) {
		return signApplyService.fetchTeam(id);
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

	@RequestMapping("/single/update")
	public ApiResponse<?> teamDemoExport(@RequestBody SignSingleUpdateQuery request) {
		request.setGameId(ThreadLocalUtil.getCurrentGameId());
		request.setUserId(ThreadLocalUtil.getCurrentUserId());
		return signApplyService.signUpdate(request);
	}

	@GetMapping("/org/list")
	public ApiResponse<?> getOrgList() {
		Long gameId = ThreadLocalUtil.getCurrentGameId();
		return signApplyService.getOrgList(gameId);
	}

}

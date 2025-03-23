package com.jsnjwj.facade.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.request.BaseRequest;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.common.utils.ThreadLocalUtil;
import com.jsnjwj.facade.dto.SignSingleDto;
import com.jsnjwj.facade.dto.SignTeamDto;
import com.jsnjwj.facade.easyexcel.upload.ImportSingleUploadDto;
import com.jsnjwj.facade.easyexcel.upload.ImportTeamUploadDto;
import com.jsnjwj.facade.entity.*;
import com.jsnjwj.facade.excel.SingleImportListener;
import com.jsnjwj.facade.excel.TeamImportListener;
import com.jsnjwj.facade.manager.SignApplyManager;
import com.jsnjwj.facade.query.SignSingleListQuery;
import com.jsnjwj.facade.query.SignSingleUpdateQuery;
import com.jsnjwj.facade.query.SignTeamListQuery;
import com.jsnjwj.facade.query.SignTeamUpdateQuery;
import com.jsnjwj.facade.service.SignApplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 合同比对service
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SignApplyServiceImpl implements SignApplyService {

    private final SignApplyManager signApplyManager;

    @Override
    public ApiResponse<?> fetchSinglePage(SignSingleListQuery query) {
        Page<SignSingleDto> page = new Page<>();
        List<SignSingleDto> list = signApplyManager.fetchSignSinglePage(query);
        list.forEach(signRecord -> {
            GameGroupEntity groupEntity = signApplyManager.getGroupById(signRecord.getGroupId());
            GameItemEntity itemEntity = signApplyManager.getItemById(signRecord.getItemId());
            SignTeamEntity teamEntity = signApplyManager.getTeamById(signRecord.getTeamId());
            if (Objects.nonNull(groupEntity)) {
                signRecord.setGroupName(groupEntity.getGroupName());
            }
            if (Objects.nonNull(itemEntity)) {
                signRecord.setItemName(itemEntity.getItemName());
            }

            if (Objects.nonNull(teamEntity)) {
                SignTeamDto teamDto = new SignTeamDto();
                teamDto.setTeamName(teamEntity.getTeamName());
                teamDto.setCoachName(teamEntity.getCoachName());
                teamDto.setCoachTel(teamEntity.getCoachTel());
                teamDto.setLeaderName(teamEntity.getLeaderName());
                teamDto.setLeaderTel(teamEntity.getLeaderTel());
                signRecord.setTeam(teamDto);

            }
        });
        page.setRecords(list);
        page.setTotal(signApplyManager.fetchSignSingleCount(query));
        return ApiResponse.success(page);
    }

    @Override
    public ApiResponse<?> fetchTeamPage(SignTeamListQuery query) {
        query.setGameId(ThreadLocalUtil.getCurrentGameId());
        Page<SignTeamEntity> page = signApplyManager.fetchSignTeamPage(query);
        List<SignTeamDto> teamDtoList = new ArrayList<>();
        if (!page.getRecords().isEmpty()) {
            for (SignTeamEntity entity : page.getRecords()) {
                SignTeamDto signTeamDto = new SignTeamDto();
                BeanUtil.copyProperties(entity, signTeamDto);
                signTeamDto.setTeamId(entity.getId());
                signTeamDto.setOrgId(entity.getOrgId());
                Long orgId = entity.getOrgId();
                if (Objects.nonNull(orgId) && orgId > 0L) {
                    SignOrgEntity orgEntity = signApplyManager.getSignOrgById(orgId);
                    if (Objects.nonNull(orgEntity)) {
                        signTeamDto.setOrgName(orgEntity.getOrgName());
                    }
                }
                teamDtoList.add(signTeamDto);

            }
        }

        Page<SignTeamDto> response = new Page<>(query.getPage(), query.getLimit());
        response.setRecords(teamDtoList);
        response.setTotal(page.getTotal());

        return ApiResponse.success(response);
    }

    @Override
    public ApiResponse<?> fetchTeamData(SignTeamListQuery query) {
        query.setGameId(ThreadLocalUtil.getCurrentGameId());
        List<SignTeamEntity> page = signApplyManager.fetchSignTeamData(query);

        return ApiResponse.success(page);
    }

    @Override
    public ApiResponse<?> updateTeam(SignTeamUpdateQuery query) {
        SignTeamEntity signTeamEntity = new SignTeamEntity();
        signTeamEntity.setId(query.getTeamId());
        signTeamEntity.setTeamName(query.getTeamName());
        int result = signApplyManager.updateTeam(signTeamEntity);

        SignOrgEntity signOrgEntity = new SignOrgEntity();
        signOrgEntity.setId(query.getOrgId());
        signOrgEntity.setOrgName(query.getOrgName());
        signApplyManager.updateOrg(signOrgEntity);

        return ApiResponse.success(result > 0);
    }

    @Override
    public ApiResponse<?> deleteTeam(Long teamId) {
        // 校验该队伍下面，是否有选手
        SignSingleListQuery query = new SignSingleListQuery();
        query.setTeamId(teamId);
        long signSingleCount = signApplyManager.fetchSignSingleCount(query);
        if (signSingleCount > 0) {
            ApiResponse response = new ApiResponse<>();
            response.setMessage("请先删除该队伍下所有选手");
            response.setData(false);
            return response;
        }
        int result = signApplyManager.deleteTeam(teamId);
        return ApiResponse.success(result > 0);
    }

    @Override
    public ApiResponse<?> fetchTeam(Long teamId) {
        SignTeamEntity result = signApplyManager.getTeamById(teamId);
        return ApiResponse.success(result);
    }

    @Override
    public ApiResponse<?> importTeam(BaseRequest request, MultipartFile file) throws IOException {
        InputStream is = file.getInputStream();

        TeamImportListener userReadListener = new TeamImportListener(request.getGameId(), signApplyManager);
        EasyExcel.read(is, ImportTeamUploadDto.class, userReadListener).sheet(0).headRowNumber(1).doRead();
        return ApiResponse.success();
    }

    @Override
    public ApiResponse<?> exportTeamDemo(BaseRequest baseRequest, MultipartFile file) throws IOException {
        return null;
    }

    @Override
    public ApiResponse<?> importSingle(Integer importType, MultipartFile file) {

        try {
            // 初始化监听器
            SingleImportListener singleImportListener = new SingleImportListener(ThreadLocalUtil.getCurrentGameId(),
                    signApplyManager);
            // 解析数据
            EasyExcelFactory.read(file.getInputStream(), singleImportListener)
                    .head(ImportSingleUploadDto.class)
                    .headRowNumber(1)
                    .sheet(0)
                    .doReadSync();

        } catch (Exception e) {
            log.error("importSingle exception", e);
        }

        return ApiResponse.success();
    }

    @Override
    public ApiResponse<?> exportSingleDemo(BaseRequest baseRequest, MultipartFile file) {
        return null;
    }

    @Override
    public ApiResponse<?> signDelete(Long signId) {
        int res = signApplyManager.deleteById(signId);
        return ApiResponse.success(res > 0);
    }

    @Override
    public ApiResponse<?> signDeleteBatch(List<Long> signIds) {
        int res = signApplyManager.deleteByIds(signIds);
        return ApiResponse.success(res > 0);
    }

    @Override
    public ApiResponse<?> signUpdate(SignSingleUpdateQuery request) {
        SignSingleEntity signSingleEntity = new SignSingleEntity();
        signSingleEntity.setId(request.getApplyId());
        signSingleEntity.setName(request.getName());
        signSingleEntity.setAge(request.getAge());
        signSingleEntity.setSex(request.getSex());
        signSingleEntity.setOrgName(request.getOrgName());
        int result = signApplyManager.updateSingle(signSingleEntity);
        return ApiResponse.success(result > 0);
    }

    @Override
    public ApiResponse<?> getOrgList(Long gameId) {
        return ApiResponse.success(signApplyManager.getOrgList(gameId));
    }

}

package com.jsnjwj.facade.service.impl;

import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.dto.GameAreaDto;
import com.jsnjwj.facade.dto.GameItemDto;
import com.jsnjwj.facade.dto.GroupAreaItemDto;
import com.jsnjwj.facade.entity.*;
import com.jsnjwj.facade.manager.GameGroupManager;
import com.jsnjwj.facade.manager.GameGroupingManager;
import com.jsnjwj.facade.manager.GameItemManager;
import com.jsnjwj.facade.manager.GameSettingRuleManager;
import com.jsnjwj.facade.query.*;
import com.jsnjwj.facade.service.GameGroupService;
import com.jsnjwj.facade.service.GameGroupingService;
import com.jsnjwj.facade.service.GameItemService;
import com.jsnjwj.facade.service.GameSettingService;
import com.jsnjwj.facade.vo.GameGroupAreaItemListVo;
import com.jsnjwj.facade.vo.GameRuleSettingVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 合同比对service
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class GameGroupingServiceImpl implements GameGroupingService {

}

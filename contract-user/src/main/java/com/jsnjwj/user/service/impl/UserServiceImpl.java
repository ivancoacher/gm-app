package com.jsnjwj.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.user.config.JwtConfig;
import com.jsnjwj.user.dao.OptLogDao;
import com.jsnjwj.user.dao.OrgDao;
import com.jsnjwj.user.dao.UserDao;
import com.jsnjwj.user.entity.OptLog;
import com.jsnjwj.user.entity.OrgEntity;
import com.jsnjwj.user.entity.User;
import com.jsnjwj.user.enums.OperateTypeEnum;
import com.jsnjwj.user.enums.OrganizetionStatusEnum;
import com.jsnjwj.user.reponse.UserInfoResponse;
import com.jsnjwj.user.request.FetchOptLogRequest;
import com.jsnjwj.user.request.LoginRequest;
import com.jsnjwj.user.service.UserService;
import com.jsnjwj.user.vo.OperateLogVo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

	private static final String DEFAULT_AVATAR = "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif";

	private static final String DEFAULT_INFO = "";

	@Resource
	private JwtConfig jwtConfig;

	@Resource
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Resource
	private UserDao userDao;

	@Resource
	private OptLogDao optLogDao;

	@Resource
	private OrgDao orgDao;

	@Override
	public ApiResponse login(LoginRequest request) {

		String username = request.getUsername();

		QueryWrapper<User> wrapper = new QueryWrapper<User>();
		wrapper.eq("username", username);
		User user = userDao.selectOne(wrapper);
		if (null == user) {
			return ApiResponse.error("用户信息不存在");
		}
		String password = user.getPassword();
		if (bCryptPasswordEncoder.matches(password, bCryptPasswordEncoder.encode(password))) {
			saveOptLog(user.getId(), OperateTypeEnum.LOGIN, "");
			return ApiResponse.success(jwtConfig.createToken(String.valueOf(user.getId())));
		}
		return ApiResponse.error("登录失败");
	}

	private void saveOptLog(Long userId, OperateTypeEnum operateTypeEnum, String remark) {
		// 保存日志
		OptLog optLog = new OptLog();
		optLog.setUserId(userId);
		optLog.setOperateType(operateTypeEnum.getCode());
		optLog.setRemark(remark);
		optLog.setCreateTime(new Date());
		optLogDao.insert(optLog);
	}

	@Override
	public ApiResponse register(LoginRequest request) {

		String username = request.getUsername();
		LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		lambdaQueryWrapper.eq(User::getUsername, username);
		User checkUser = userDao.selectOne(lambdaQueryWrapper);
		if (null != checkUser) {
			return ApiResponse.error("用户信息已存在");
		}

		String password = bCryptPasswordEncoder.encode(request.getPassword());
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setAvatar(DEFAULT_AVATAR);
		user.setInfo(DEFAULT_INFO);
		user.setRole(request.getRole());
		user.setCreateTime(new Date());
		userDao.insert(user);
		saveOptLog(user.getId(), OperateTypeEnum.REGISTER, "");

		return ApiResponse.success("注册成功");
	}

	@Override
	public ApiResponse info(Long userId) {
		User user = userDao.selectById(userId);
		if (null == user) {
			return ApiResponse.error("用户信息不存在");
		}
		UserInfoResponse response = new UserInfoResponse();
		response.setUserId(user.getId());
		response.setName(user.getUsername());
		response.setAvatar(user.getAvatar());
		response.setOrganizationId(user.getOrganizationId());
		response.setOrganizationStatus(user.getOrganizationStatus());

		// 查询用户所属组织
		if (user.getOrganizationStatus() > 0) {
			OrgEntity organization = orgDao.selectById(user.getOrganizationId());
			if (!Objects.isNull(organization)) {
				response.setOrganizationName(organization.getOrgName());
			}
		}
		List<String> roles = new ArrayList<>();
		roles.add(user.getRole());
		response.setRoles(roles);
		response.setInfo(user.getInfo());
		return ApiResponse.success(response);
	}

	@Override
	public ApiResponse<Page<OperateLogVo>> fetchOptLogList(FetchOptLogRequest request) {
		User user = userDao.selectById(request.getUserId());
		if (null == user) {
			return ApiResponse.error("用户信息不存在");
		}
		Page<OptLog> result = new Page<>();
		result.setCurrent(request.getPageIndex());
		result.setSize(request.getPageSize());
		QueryWrapper<OptLog> wrapper = new QueryWrapper<>();
		wrapper.lambda().eq(OptLog::getUserId, request.getUserId());

		wrapper.lambda().gt(!Objects.isNull(request.getStartTime()), OptLog::getCreateTime, request.getStartTime());

		if (!Objects.isNull(request.getEndTime())) {
			wrapper.lambda().lt(OptLog::getCreateTime, request.getEndTime() + " 23:59:59");
		}
		wrapper.lambda().orderByDesc(OptLog::getCreateTime);
		result = optLogDao.selectPage(result, wrapper);

		Page<OperateLogVo> response = new Page<>();
		response.setCurrent(result.getSize());
		response.setTotal(result.getTotal());
		response.setPages(result.getPages());
		List<OperateLogVo> records = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(result.getRecords())) {
			List<Long> customerIds = result.getRecords()
				.stream()
				.map(OptLog::getUserId)
				.distinct()
				.collect(Collectors.toList());
			QueryWrapper<User> wrapper1 = new QueryWrapper<>();
			wrapper1.lambda().in(User::getId, customerIds);
			List<User> userList = userDao.selectList(wrapper1);
			Map<Long, String> userMap = userList.stream().collect(Collectors.toMap(User::getId, User::getUsername));

			result.getRecords().forEach(record -> {
				OperateLogVo vo = new OperateLogVo();
				vo.setUserId(record.getUserId());
				vo.setId(record.getId());
				vo.setCreateTime(record.getCreateTime());
				if (userMap.containsKey(record.getUserId())) {
					vo.setUsername(userMap.get(record.getUserId()));
				}
				vo.setOperateType(record.getOperateType());
				vo.setOperateTypeDesc(OperateTypeEnum.getValue(record.getOperateType()));
				vo.setRemark(record.getRemark());
				records.add(vo);
			});
		}
		response.setRecords(records);

		return ApiResponse.success(response);
	}

	/**
	 * 校验用户当前组织认证信息
	 */
	private Boolean checkUserOrgStatus(User user) {
		return user.getOrganizationId() > 0
				&& OrganizetionStatusEnum.AUTHENTICATED.getCode() == user.getOrganizationStatus();
	}

}

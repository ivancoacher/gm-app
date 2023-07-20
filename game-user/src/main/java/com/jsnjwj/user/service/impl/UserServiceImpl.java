package com.jsnjwj.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.user.config.JwtConfig;
import com.jsnjwj.user.dao.AdminUserDao;
import com.jsnjwj.user.dao.OrgDao;
import com.jsnjwj.user.dao.TcOptLogDao;
import com.jsnjwj.user.entity.AdminUser;
import com.jsnjwj.user.entity.TcOptLog;
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
	private AdminUserDao adminUserDao;

	@Resource
	private TcOptLogDao optLogDao;

	@Resource
	private OrgDao orgDao;

	@Override
	public ApiResponse login(LoginRequest request) {

		String username = request.getUsername();

		QueryWrapper<AdminUser> wrapper = new QueryWrapper<>();
		wrapper.eq("username", username);
		AdminUser user = adminUserDao.selectOne(wrapper);
		if (null == user) {
			return ApiResponse.error("用户信息不存在");
		}
		String password = user.getPasswordHash();
		if (bCryptPasswordEncoder.matches(password, bCryptPasswordEncoder.encode(password))) {
			saveOptLog(user.getId(), OperateTypeEnum.LOGIN, "");
			return ApiResponse.success(jwtConfig.createToken(String.valueOf(user.getId())));
		}
		return ApiResponse.error("登录失败");
	}

	private void saveOptLog(Long userId, OperateTypeEnum operateTypeEnum, String remark) {
		// 保存日志
		TcOptLog optLog = new TcOptLog();
		optLog.setUserId(userId);
		optLog.setOperateType(operateTypeEnum.getCode());
		optLog.setRemark(remark);
		optLog.setCreateTime(new Date());
		optLogDao.insert(optLog);
	}

	@Override
	public ApiResponse register(LoginRequest request) {

		String username = request.getUsername();
		LambdaQueryWrapper<AdminUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		lambdaQueryWrapper.eq(AdminUser::getUsername, username);
		AdminUser checkUser = adminUserDao.selectOne(lambdaQueryWrapper);
		if (null != checkUser) {
			return ApiResponse.error("用户信息已存在");
		}

		String password = bCryptPasswordEncoder.encode(request.getPassword());
		AdminUser user = new AdminUser();
		user.setUsername(username);
		user.setPasswordHash(password);
		user.setAvatar(DEFAULT_AVATAR);
		user.setRemark(DEFAULT_INFO);
		// user.setRole(request.getRole());
		user.setCreateAt(new Date());
		adminUserDao.insert(user);
		saveOptLog(user.getId(), OperateTypeEnum.REGISTER, "");

		return ApiResponse.success("注册成功");
	}

	@Override
	public ApiResponse info(Long userId) {
		AdminUser user = adminUserDao.selectById(userId);
		if (null == user) {
			return ApiResponse.error("用户信息不存在");
		}
		UserInfoResponse response = new UserInfoResponse();
		response.setUserId(user.getId());
		response.setName(user.getUsername());
		response.setAvatar(user.getAvatar());
		List<String> roles = new ArrayList<>();
		roles.add("admin");
		response.setRoles(roles);
		response.setInfo(user.getRemark());
		response.setPhone(user.getPhone());
		return ApiResponse.success(response);
	}

	@Override
	public ApiResponse<Page<OperateLogVo>> fetchOptLogList(FetchOptLogRequest request) {
		AdminUser user = adminUserDao.selectById(request.getUserId());
		if (null == user) {
			return ApiResponse.error("用户信息不存在");
		}
		Page<TcOptLog> result = new Page<>();
		result.setCurrent(request.getPageIndex());
		result.setSize(request.getPageSize());
		QueryWrapper<TcOptLog> wrapper = new QueryWrapper<>();
		wrapper.lambda().eq(TcOptLog::getUserId, request.getUserId());

		wrapper.lambda().gt(!Objects.isNull(request.getStartTime()), TcOptLog::getCreateTime, request.getStartTime());

		if (!Objects.isNull(request.getEndTime())) {
			wrapper.lambda().lt(TcOptLog::getCreateTime, request.getEndTime() + " 23:59:59");
		}
		wrapper.lambda().orderByDesc(TcOptLog::getCreateTime);
		result = optLogDao.selectPage(result, wrapper);

		Page<OperateLogVo> response = new Page<>();
		response.setCurrent(result.getSize());
		response.setTotal(result.getTotal());
		response.setPages(result.getPages());
		List<OperateLogVo> records = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(result.getRecords())) {
			List<Long> customerIds = result.getRecords()
				.stream()
				.map(TcOptLog::getUserId)
				.distinct()
				.collect(Collectors.toList());
			QueryWrapper<AdminUser> wrapper1 = new QueryWrapper<>();
			wrapper1.lambda().in(AdminUser::getId, customerIds);
			List<AdminUser> userList = adminUserDao.selectList(wrapper1);
			Map<Long, String> userMap = userList.stream()
				.collect(Collectors.toMap(AdminUser::getId, AdminUser::getUsername));

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

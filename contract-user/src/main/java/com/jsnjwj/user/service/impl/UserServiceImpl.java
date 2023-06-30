package com.jsnjwj.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.user.config.JwtConfig;
import com.jsnjwj.user.dao.UserDao;
import com.jsnjwj.user.entity.User;
import com.jsnjwj.user.request.LoginRequest;
import com.jsnjwj.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

	@Resource
	private JwtConfig jwtConfig;

	@Resource
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Resource
	private UserDao userDao;

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
			return ApiResponse.success(jwtConfig.createToken(String.valueOf(user.getId())));
		}
		return ApiResponse.error("登录失败");
	}

	@Override
	public ApiResponse register(LoginRequest request) {

		String username = request.getUsername();
		String password = bCryptPasswordEncoder.encode(request.getPassword());
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		userDao.insert(user);
		return ApiResponse.success("注册成功");
	}

}

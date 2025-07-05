package com.jsnjwj.api.interceptor;

import com.jsnjwj.common.utils.ThreadLocalUtil;
import com.jsnjwj.user.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureException;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @author chenmingyong
 */
@Component
public class AccessTokenInterceptor implements HandlerInterceptor {

	@Resource
	private JwtConfig jwtConfig;

	@Override
	public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler)
			throws Exception {

		if (RequestMethod.OPTIONS.name().equals(request.getMethod())) {
			// response.setHeader("Cache-Control","no-cache");
			response.setHeader("Access-control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
			response.setHeader("Access-Control-Allow-Headers", "*");
			// 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
			response.setStatus(HttpStatus.OK.value());
			return true;
		}

		/** 地址过滤 */
		String uri = request.getRequestURI();
		if (uri.contains("/auth/login") || uri.contains("/auth/register") || uri.contains("/file")) {
			return true;
		}
		if (uri.contains("vote")) {
			return true;
		}

		if (uri.contains("single/importExample")) {
			return true;
		}

		/** Token 验证 */
		String token = request.getHeader(jwtConfig.getHeader());
		if (null == token || StringUtils.isEmpty(token)) {
			throw new SignatureException(jwtConfig.getHeader() + "不能为空");
		}
		if (StringUtils.isEmpty(token)) {
			token = request.getParameter(jwtConfig.getHeader());
		}

		Claims claims = null;
		try {
			claims = jwtConfig.getTokenClaim(token);
			if (claims == null || jwtConfig.isTokenExpired(claims.getExpiration())) {
				throw new SignatureException(jwtConfig.getHeader() + "失效，请重新登录。");
			}
			String gameId = request.getHeader("game-id");

			ThreadLocalUtil.addCurrentUser(Long.valueOf(claims.getSubject()));
			ThreadLocalUtil.addCurrentGame(Objects.nonNull(gameId) ? Long.parseLong(gameId) : 0L);

		}
		catch (Exception e) {
			throw new SignatureException(jwtConfig.getHeader() + "失效，请重新登录。");
		}
		return true;
	}

}

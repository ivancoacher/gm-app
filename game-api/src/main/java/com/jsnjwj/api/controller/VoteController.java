package com.jsnjwj.api.controller;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsnjwj.api.utils.WeixinCheckoutUtil;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.entity.VoteDetailEntity;
import com.jsnjwj.facade.entity.VoteSchoolEntity;
import com.jsnjwj.facade.mapper.VoteDetailMapper;
import com.jsnjwj.facade.mapper.VoteSchoolMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/game/vote")
public class VoteController {

	private final VoteSchoolMapper voteSchoolMapper;

	private final VoteDetailMapper voteDetailMapper;

	private final RestTemplate restTemplate;

	@GetMapping("/checkToken")
	public String checkToken(HttpServletRequest request, HttpServletResponse response) {
		// 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
		String signature = request.getParameter("signature");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		// 随机字符串
		String echostr = request.getParameter("echostr");

		PrintWriter out = null;
		try {
			out = response.getWriter();
			// 通过检验signature对请求进行校验，若校验成功则原样返回echostr，否则接入失败
			if (WeixinCheckoutUtil.checkSignature(signature, timestamp, nonce)) {
				out.print(echostr);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			out.close();
			out = null;
		}
		return null;
	}

	@GetMapping("/getOpenId")
	public Object getOpenId(@RequestParam(value = "code", required = true) String code) {
		log.info("getOpenId request:{}", code);
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx1134ab48a212a028&secret=a5646f0f9f8875948bdc93f91d7088b7&code="
				+ code + "&grant_type=authorization_code";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<Object> entity = new HttpEntity<>(null, headers);
		String result = restTemplate.exchange(url, HttpMethod.GET, null, String.class).getBody();
		log.info("getOpenId response:{}", JSON.toJSONString(result));
		return ApiResponse.success(JSON.parseObject(result, Map.class));
	}

	@GetMapping("/list")
	public ApiResponse<?> list(@RequestParam(value = "area", required = false) Integer area) {
		Map<String, Object> result = new HashMap<>();
		Wrapper<VoteDetailEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		long total = voteDetailMapper.selectCount(lambdaQueryWrapper);

		result.put("total", total);

		// 1_1,1;2,1;3,1;4,0;5,0;6,0;7,0;8,0;9,0;10,0;11,0;12,0;13,0;14,0;15,0;16,0;17,0;18,0;19,0;20,0;21,0;22,0;23,0;24,0;25,0;26,0;27,0;28,0;29,0;30,0;31,0;32,0;33,0;34,0;35,0;36,0;37,0;38,0;39,0;40,0;41,0;42,0;43,0;44,0;45,0;46,0;47,0;48,0;49,0;50,0;51,0;52,0;53,0;54,0;55,0;56,0;57,0;58,0;59,0;60,0;61,0;62,0;63,0;64,0;65,0;66,0;67,0;68,0;69,0;70,0;71,0;72,0;73,0;74,0;75,0;76,0;77,0;78,0;79,0;80,0;81,0;82,0;83,0;84,0;85,0;86,0:
		// 2_79,1;1,0;2,0;3,0;4,0;5,0;6,0;7,0;8,0;9,0;10,0;11,0;12,0;13,0;14,0;15,0;16,0;17,0;18,0;19,0;20,0;21,0;22,0;23,0;24,0;25,0;26,0;27,0;28,0;29,0;30,0;31,0;32,0;33,0;34,0;35,0;36,0;37,0;38,0;39,0;40,0;41,0;42,0;43,0;44,0;45,0;46,0;47,0;48,0;49,0;50,0;51,0;52,0;53,0;54,0;55,0;56,0;57,0;58,0;59,0;60,0;61,0;62,0;63,0;64,0;65,0;66,0;67,0;68,0;69,0;70,0;71,0;72,0;73,0;74,0;75,0;76,0;77,0;78,0;80,0;81,0;82,0;83,0;84,0;85,0;86,0

		LambdaQueryWrapper<VoteSchoolEntity> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
		lambdaQueryWrapper1.eq(Objects.nonNull(area), VoteSchoolEntity::getArea, area);
		List<VoteSchoolEntity> schoolEntities = voteSchoolMapper.selectList(lambdaQueryWrapper1);
		StringBuffer data = new StringBuffer();
		data.append("1_");
		schoolEntities.forEach(schoolEntity -> {
			LambdaQueryWrapper<VoteDetailEntity> query1 = new LambdaQueryWrapper<>();
			query1.eq(VoteDetailEntity::getSchoolId, schoolEntity.getId());
			query1.eq(VoteDetailEntity::getType, 1);
			Long count = voteDetailMapper.selectCount(query1);
			schoolEntity.setVoteNum(count);
			data.append(schoolEntity.getId()).append(",").append(schoolEntity.getVoteNum()).append(";");
		});
		data.append(":2_");
		schoolEntities.forEach(schoolEntity -> {
			LambdaQueryWrapper<VoteDetailEntity> query2 = new LambdaQueryWrapper<>();
			query2.eq(VoteDetailEntity::getSchoolId, schoolEntity.getId());
			query2.eq(VoteDetailEntity::getType, 2);
			Long count = voteDetailMapper.selectCount(query2);
			schoolEntity.setVoteNum(count);
			data.append(schoolEntity.getId()).append(",").append(schoolEntity.getVoteNum()).append(";");
		});
		result.put("data", data.toString());
		return ApiResponse.success(result);
	}

	@PostMapping("/do")
	public ApiResponse vote(@RequestParam String data, @RequestParam String openid) {
		log.info("vote request:{}", JSON.toJSONString(data));

		if (!StringUtils.isEmpty(openid)) {
			// 判断用户是否已经投票
			LambdaQueryWrapper<VoteDetailEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
			lambdaQueryWrapper.eq(VoteDetailEntity::getOpenid, openid);
			lambdaQueryWrapper.last("limit 1");
			VoteDetailEntity result = voteDetailMapper.selectOne(lambdaQueryWrapper);
			if (Objects.nonNull(result)) {
				return ApiResponse.error("您已投票");
			}

		}

		if (!StringUtils.isEmpty(data)) {
			String[] list = data.split("=");

			if (Objects.nonNull(list[0]) && com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(list[0])) {
				VoteDetailEntity voteDetailEntity = new VoteDetailEntity();
				voteDetailEntity.setSchoolId(Long.parseLong(list[0]));
				voteDetailEntity.setType(1);
				voteDetailEntity.setOpenid(openid);
				voteDetailMapper.insert(voteDetailEntity);

				VoteSchoolEntity voteSchoolEntity = voteSchoolMapper.selectById(Long.parseLong(list[0]));
				voteSchoolEntity.setVoteNum(voteSchoolEntity.getVoteNum() + 1);
				voteSchoolMapper.updateById(voteSchoolEntity);
			}
			if (Arrays.stream(list).count() > 1 && Objects.nonNull(list[1])
					&& com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(list[1])) {
				if (!"undefined".equals(list[1])) {
					VoteDetailEntity voteDetailEntity = new VoteDetailEntity();
					voteDetailEntity.setSchoolId(Long.parseLong(list[1]));
					voteDetailEntity.setOpenid(openid);
					voteDetailEntity.setType(2);
					voteDetailMapper.insert(voteDetailEntity);

					VoteSchoolEntity voteSchoolEntity = voteSchoolMapper.selectById(Long.parseLong(list[1]));
					voteSchoolEntity.setVoteNum(voteSchoolEntity.getVoteNum() + 1);
					voteSchoolMapper.updateById(voteSchoolEntity);

				}
			}

		}

		return ApiResponse.success();
	}

}

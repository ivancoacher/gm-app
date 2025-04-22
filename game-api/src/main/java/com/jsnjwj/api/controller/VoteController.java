package com.jsnjwj.api.controller;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsnjwj.common.response.ApiResponse;
import com.jsnjwj.facade.entity.VoteDetailEntity;
import com.jsnjwj.facade.entity.VoteSchoolEntity;
import com.jsnjwj.facade.mapper.VoteDetailMapper;
import com.jsnjwj.facade.mapper.VoteSchoolMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/game/vote")
public class VoteController {

	private final VoteSchoolMapper voteSchoolMapper;

	private final VoteDetailMapper voteDetailMapper;

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
			data.append(schoolEntity.getId()).append(",").append(schoolEntity.getVoteNum()).append(";");
		});
		data.append(":2_");
		schoolEntities.forEach(schoolEntity -> {
			data.append(schoolEntity.getId()).append(",").append(schoolEntity.getVoteNum()).append(";");
		});
		result.put("data", data.toString());
		return ApiResponse.success(result);
	}

	@PostMapping("/do")
	public ApiResponse vote(@RequestParam String data) {
		log.info("vote request:{}", JSON.toJSONString(data));
		if (!StringUtils.isEmpty(data)) {
			String[] list = data.split("=");
			if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(list[0])) {
				VoteDetailEntity voteDetailEntity = new VoteDetailEntity();
				voteDetailEntity.setSchoolId(Long.parseLong(list[0]));
				voteDetailEntity.setType(1);
				voteDetailMapper.insert(voteDetailEntity);

				VoteSchoolEntity voteSchoolEntity = voteSchoolMapper.selectById(Long.parseLong(list[0]));
				voteSchoolEntity.setVoteNum(voteSchoolEntity.getVoteNum() + 1);
				voteSchoolMapper.updateById(voteSchoolEntity);
			}
			if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(list[1])) {
				VoteDetailEntity voteDetailEntity = new VoteDetailEntity();
				voteDetailEntity.setSchoolId(Long.parseLong(list[1]));
				voteDetailEntity.setType(2);
				voteDetailMapper.insert(voteDetailEntity);

				VoteSchoolEntity voteSchoolEntity = voteSchoolMapper.selectById(Long.parseLong(list[1]));
				voteSchoolEntity.setVoteNum(voteSchoolEntity.getVoteNum() + 1);
				voteSchoolMapper.updateById(voteSchoolEntity);

			}

		}

		return ApiResponse.success();
	}

}

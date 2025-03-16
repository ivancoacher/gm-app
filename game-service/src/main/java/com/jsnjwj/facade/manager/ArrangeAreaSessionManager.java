package com.jsnjwj.facade.manager;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsnjwj.facade.entity.ArrangeAreaSessionEntity;
import com.jsnjwj.facade.entity.GameSessionItemEntity;
import com.jsnjwj.facade.mapper.ArrangeAreaSessionMapper;
import com.jsnjwj.facade.mapper.GameSessionItemMapper;
import com.jsnjwj.facade.vo.session.SessionItemVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArrangeAreaSessionManager {

	private final ArrangeAreaSessionMapper arrangeAreaSessionMapper;

	public boolean checkSessionItemExist(Long gameId, Long sessionId) {
		return arrangeAreaSessionMapper.checkSessionItemExist(gameId, sessionId) > 0;
	}

	public void deleteBySessionId(Long gameId, Long sessionId) {
		LambdaQueryWrapper<ArrangeAreaSessionEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		lambdaQueryWrapper.eq(ArrangeAreaSessionEntity::getGameId, gameId);
		lambdaQueryWrapper.eq(Objects.nonNull(sessionId), ArrangeAreaSessionEntity::getSessionId, sessionId);
		arrangeAreaSessionMapper.delete(lambdaQueryWrapper);
	}

}

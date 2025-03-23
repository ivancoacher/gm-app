package com.jsnjwj.facade.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsnjwj.facade.entity.ArrangeAreaSessionEntity;
import com.jsnjwj.facade.mapper.ArrangeAreaSessionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

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

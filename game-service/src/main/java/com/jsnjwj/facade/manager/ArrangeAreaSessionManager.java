package com.jsnjwj.facade.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsnjwj.facade.dto.ArrangeSessionVo;
import com.jsnjwj.facade.entity.ArrangeAreaSessionEntity;
import com.jsnjwj.facade.mapper.ArrangeAreaSessionMapper;
import com.jsnjwj.facade.vo.AreaSessionVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

	public void deleteByAreaId(Long gameId, Long areaId) {
		LambdaQueryWrapper<ArrangeAreaSessionEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		lambdaQueryWrapper.eq(ArrangeAreaSessionEntity::getGameId, gameId);
		lambdaQueryWrapper.eq(Objects.nonNull(areaId), ArrangeAreaSessionEntity::getAreaId, areaId);
		arrangeAreaSessionMapper.delete(lambdaQueryWrapper);
	}

	public List<AreaSessionVo> getSessionByAreaId(Long gameId, Long areaId) {
		return arrangeAreaSessionMapper.selectSessionByAreaId(gameId, areaId);
	}

	/**
	 * 查询已排场地场次
	 * @param gameId
	 * @return
	 */
	public List<ArrangeAreaSessionEntity> selectArrangedSession(Long gameId) {

		LambdaQueryWrapper<ArrangeAreaSessionEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.select(ArrangeAreaSessionEntity::getSessionId);
		queryWrapper.eq(ArrangeAreaSessionEntity::getGameId, gameId);
		return arrangeAreaSessionMapper.selectList(queryWrapper);
	}

}

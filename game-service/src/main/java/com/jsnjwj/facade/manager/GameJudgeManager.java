package com.jsnjwj.facade.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsnjwj.facade.entity.GameJudgeEntity;
import com.jsnjwj.facade.entity.GameJudgeItemEntity;
import com.jsnjwj.facade.mapper.TcGameJudgeItemMapper;
import com.jsnjwj.facade.mapper.TcGameJudgeMapper;
import com.jsnjwj.facade.vo.GameJudgeVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GameJudgeManager {

	@Resource
	private TcGameJudgeMapper gameJudgeMapper;

	@Resource
	private TcGameJudgeItemMapper gameJudgeItemMapper;

	/**
	 * 根据赛事编号查询裁判列表
	 * @param gameId
	 * @return
	 */
	public List<GameJudgeVo> fetchGameJudges(Long gameId) {
		return gameJudgeMapper.selectPageList(gameId);
	}

	public Long fetchGameJudgesCount(Long gameId) {
		LambdaQueryWrapper<GameJudgeEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(GameJudgeEntity::getGameId, gameId);
		return gameJudgeMapper.selectCount(wrapper);
	}

	/**
	 * 查询赛事项目下已分配的裁判列表
	 * @param gameId
	 * @param itemId
	 * @return
	 */
	public List<GameJudgeItemEntity> fetchItemJudge(Long gameId, Long itemId) {
		LambdaQueryWrapper<GameJudgeItemEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(GameJudgeItemEntity::getGameId, gameId);
		wrapper.eq(GameJudgeItemEntity::getItemId, itemId);
		return gameJudgeItemMapper.selectList(wrapper);
	}

	public List<GameJudgeVo> fetchList(Long gameId) {
		return gameJudgeMapper.selectAllList(gameId);
	}

	/**
	 * 保存裁判信息
	 * @param gameJudgeEntity
	 * @return
	 */
	public int save(GameJudgeEntity gameJudgeEntity) {
		return gameJudgeMapper.insert(gameJudgeEntity);
	}

	/**
	 * 更新裁判信息
	 * @param gameJudgeEntity
	 * @return
	 */
	public int update(GameJudgeEntity gameJudgeEntity) {
		return gameJudgeMapper.updateById(gameJudgeEntity);
	}

	/**
	 * 删除裁判信息
	 * @param gameJudgeEntity
	 * @return
	 */
	public int delete(GameJudgeEntity gameJudgeEntity) {
		return gameJudgeMapper.deleteById(gameJudgeEntity);
	}

	/**
	 * 指定项目踩盘
	 * @param tcGameJudge
	 * @return
	 */
	public int assign(GameJudgeItemEntity tcGameJudge) {
		return gameJudgeItemMapper.insert(tcGameJudge);
	}

}

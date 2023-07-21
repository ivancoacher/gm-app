package com.jsnjwj.facade.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsnjwj.facade.entity.TcGameJudge;
import com.jsnjwj.facade.entity.TcGameJudgeItem;
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
		LambdaQueryWrapper<TcGameJudge> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(TcGameJudge::getGameId, gameId);
		return gameJudgeMapper.selectCount(wrapper);
	}

	/**
	 * 查询赛事项目下已分配的裁判列表
	 * @param gameId
	 * @param itemId
	 * @return
	 */
	public List<TcGameJudgeItem> fetchItemJudge(Long gameId, Long itemId) {
		LambdaQueryWrapper<TcGameJudgeItem> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(TcGameJudgeItem::getGameId, gameId);
		wrapper.eq(TcGameJudgeItem::getItemId, itemId);
		return gameJudgeItemMapper.selectList(wrapper);
	}

	public List<GameJudgeVo> fetchList(Long gameId) {
		return gameJudgeMapper.selectAllList(gameId);
	}

	/**
	 * 保存裁判信息
	 * @param tcGameJudge
	 * @return
	 */
	public int save(TcGameJudge tcGameJudge) {
		return gameJudgeMapper.insert(tcGameJudge);
	}

	/**
	 * 更新裁判信息
	 * @param tcGameJudge
	 * @return
	 */
	public int update(TcGameJudge tcGameJudge) {
		return gameJudgeMapper.updateById(tcGameJudge);
	}

	/**
	 * 删除裁判信息
	 * @param tcGameJudge
	 * @return
	 */
	public int delete(TcGameJudge tcGameJudge) {
		return gameJudgeMapper.deleteById(tcGameJudge);
	}

	/**
	 * 指定项目踩盘
	 * @param tcGameJudge
	 * @return
	 */
	public int assign(TcGameJudgeItem tcGameJudge) {
		return gameJudgeItemMapper.insert(tcGameJudge);
	}

}

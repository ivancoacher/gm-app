package com.jsnjwj.facade.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class GameRuleSettingVo implements Serializable {

	public List<GameRuleDetailVo> detailVoList;

	private Long id;

	/**
	 *
	 */
	private Long gameId;

	/**
	 *
	 */
	private Long itemId;

	/**
	 *
	 */
	private Integer judgeGroupNum;

	/**
	 *
	 */
	private Integer scoreRule;

	/**
	 *
	 */
	private Date createdAt;

	/**
	 *
	 */
	private Date updatedAt;

	@Data
	public static class GameRuleDetailVo {

		private Integer id;

		/**
		 *
		 */
		private Long settingId;

		/**
		 *
		 */
		private Integer num;

		/**
		 *
		 */
		private Integer scoreRatio;

		/**
		 *
		 */
		private Integer scoreWeight;

		/**
		 *
		 */
		private Integer extraType;

		/**
		 *
		 */
		private String extraName;

		/**
		 *
		 */
		private Date createdAt;

		/**
		 *
		 */
		private Date updatedAt;

	}

}
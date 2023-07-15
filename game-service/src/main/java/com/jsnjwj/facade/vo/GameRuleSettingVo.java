package com.jsnjwj.facade.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class GameRuleSettingVo implements Serializable {

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

	public List<GameRuleDetailVo> detailVoList;

	@Data
	public static class GameRuleDetailVo {

		private Integer id;

		/**
		 *
		 */
		private Integer settingId;

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
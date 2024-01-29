package com.jsnjwj.facade.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class GameItemVo implements Serializable {

	private Integer id;

	/**
	 *
	 */
	private String gameName;

	/**
	 *
	 */
	private String smallTitle;

	/**
	 *
	 */
	private String gameType;

	/**
	 *
	 */
	private Integer signType;

	/**
	 * 比赛时间
	 */
	private String gameTime;

	/**
	 * 报名开始时间
	 */
	private Date signStartTime;

	/**
	 * 报名结束时间
	 */
	private Date signEndTime;

	/**
	 *
	 */
	private Integer applyCount;

	/**
	 * -1：项目预设；0：待发布；1：报名中；2：报名结束；3：报名公示；4：进行中；5：成绩公示；6：赛事结束；7：赛事延期；8：赛事取消
	 */
	private Integer status;

	private String statusDesc;

	/**
	 * 报名对象
	 */
	private String signObject;

	/**
	 * 赛事规程
	 */
	private String ruleUrl;

	/**
	 * 比赛地址
	 */
	private String gameLocation;

	/**
	 *
	 */
	private Integer creatorId;

	/**
	 *
	 */
	private Date createTime;

	/**
	 *
	 */
	private Date updateTime;

}
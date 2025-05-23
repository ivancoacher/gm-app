package com.jsnjwj.facade.vo.rule;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 项目规则列表
 */
@Getter
@Setter
public class GameItemRuleVo implements Serializable {

	private Long gameId;

	private Integer ruleId;

	private Long itemId;

	private String ruleName;

	private String itemName;

	private Long groupId;

	private String groupName;

	private LocalDateTime createAt;

	private LocalDateTime updateAt;

	private String itemType;

}
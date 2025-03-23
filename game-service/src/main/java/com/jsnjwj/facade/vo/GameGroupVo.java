package com.jsnjwj.facade.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class GameGroupVo implements Serializable {

	private Long gameId;

	private Long groupId;

	private String groupName;

	private Integer sort;

}
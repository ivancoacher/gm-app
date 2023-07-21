package com.jsnjwj.facade.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GameGroupVo implements Serializable {

	private Long gameId;

	private Long groupId;

	private String groupName;

	private Integer sort;

}
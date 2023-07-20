package com.jsnjwj.facade.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class GameGroupAllVo implements Serializable {

	private Long gameId;

	private Long groupId;

	private String groupName;

	private Integer sort;

	private List<ItemLabelVo> itemLabelVoList;
}
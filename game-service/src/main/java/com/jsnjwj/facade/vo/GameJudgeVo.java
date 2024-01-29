package com.jsnjwj.facade.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class GameJudgeVo implements Serializable {

	private Integer id;

	private Long gameId;

	private String judgeName;

	private String phone;

}
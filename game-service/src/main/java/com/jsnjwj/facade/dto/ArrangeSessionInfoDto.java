package com.jsnjwj.facade.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ArrangeSessionInfoDto {

	/**
	 * 已分配场次的项目数
	 */
	private Integer arrangedItemCount;

	/**
	 * 总项目数
	 */
	private Integer totalItemCount;

}

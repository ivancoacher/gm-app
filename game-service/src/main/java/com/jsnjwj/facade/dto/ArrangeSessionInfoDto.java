package com.jsnjwj.facade.dto;

import lombok.Getter;
import lombok.Setter;

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

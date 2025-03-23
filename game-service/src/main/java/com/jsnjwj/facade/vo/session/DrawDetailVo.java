package com.jsnjwj.facade.vo.session;

import com.jsnjwj.facade.dto.ArrangeDrawDto;
import lombok.Data;

import java.util.List;

@Data
public class DrawDetailVo {

	private String sessionName;

	private Long sessionId;

	private Integer sessionNo;

	private List<ArrangeDrawDto> data;

}

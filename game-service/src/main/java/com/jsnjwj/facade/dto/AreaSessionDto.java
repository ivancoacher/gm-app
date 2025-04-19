package com.jsnjwj.facade.dto;

import com.jsnjwj.facade.vo.AreaSessionVo;
import com.jsnjwj.facade.vo.session.SessionItemVo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AreaSessionDto {

	private Long gameId;

	private String areaName;

	private Long areaId;

	private List<AreaSessionVo> data = new ArrayList<>();

}

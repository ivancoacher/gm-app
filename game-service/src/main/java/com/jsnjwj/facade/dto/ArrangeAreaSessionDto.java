package com.jsnjwj.facade.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ArrangeAreaSessionDto {

    private Integer areaNo;

    private Long areaId;

    private String areaName;

    private Long gameId;

    private List<ArrangeSessionVo> sessionList = new ArrayList<>();

}

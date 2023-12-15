package com.jsnjwj.facade.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GameListVo implements Serializable {

    private Long pages;

    private Long total;

    private Long size;

    private Long current;

    private List<GameInfoVo> records;

}
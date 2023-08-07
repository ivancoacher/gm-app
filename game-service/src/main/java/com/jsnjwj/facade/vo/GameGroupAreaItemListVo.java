package com.jsnjwj.facade.vo;

import com.jsnjwj.facade.dto.GroupAreaItemDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GameGroupAreaItemListVo {

	private Long gameId;

	private List<GroupAreaItemDto> content = new ArrayList<>();

}

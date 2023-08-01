package com.jsnjwj.facade.vo;

import com.jsnjwj.facade.dto.GroupAreaItemDto;
import lombok.Data;

import java.util.List;

@Data
public class GameGroupAreaItemListVo {
    private Long gameId;
    List<GroupAreaItemDto> content;
}

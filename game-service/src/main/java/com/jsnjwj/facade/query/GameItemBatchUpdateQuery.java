package com.jsnjwj.facade.query;

import com.jsnjwj.facade.vo.ItemLabelVo;
import lombok.Data;

import java.util.List;

@Data
public class GameItemBatchUpdateQuery {

    List<ItemLabelVo> data;

}

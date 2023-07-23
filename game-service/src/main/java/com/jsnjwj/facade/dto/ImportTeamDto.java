package com.jsnjwj.facade.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ImportTeamDto {
    @ExcelProperty(value = "队伍名称")
    private String teamName;

    /**
     *
     */
    @ExcelProperty(value = "领队名")
    private String leaderName;

    /**
     *
     */
    @ExcelProperty(value = "领队电话")
    private String leaderTel;
}

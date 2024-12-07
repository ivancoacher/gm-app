package com.jsnjwj.facade.easyexcel.upload;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ImportTeamUploadDto {

    @ExcelProperty(value = "队伍名")
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

package com.jsnjwj.facade.easyexcel.upload;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ImportSingleUploadDto {

    @ExcelProperty(value = "项目")
    private String itemName;

    @ExcelProperty(value = "队伍名")
    private String teamName;

    /**
     *
     */
    @ExcelProperty(value = "姓名")
    private String name;

    /**
     *
     */
    @ExcelProperty(value = "年龄")
    private String age;

    /**
     *
     */
    @ExcelProperty(value = "性别")
    private String sex;

}

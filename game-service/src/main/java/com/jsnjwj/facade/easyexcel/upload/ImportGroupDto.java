package com.jsnjwj.facade.easyexcel.upload;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImportGroupDto {
    private String groupName;

    private Long groupId;
}

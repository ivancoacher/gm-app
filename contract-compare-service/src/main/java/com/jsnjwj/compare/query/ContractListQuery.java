package com.jsnjwj.compare.query;

import lombok.Data;

@Data
public class ContractListQuery {
    private Integer pageIndex = 1;

    private Integer pageSize = 10;

    private Integer userId=1;
}

package com.jsnjwj.compare.response;

import lombok.Data;

@Data
public class CompareAnalysisResponse {

    private Long totalCount;

    private Long successCount;

    private Long failureCount;

    private Long doingCount;
}

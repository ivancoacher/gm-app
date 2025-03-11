package com.jsnjwj.facade.query.session;

import com.jsnjwj.common.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ManualDrawQuery extends BaseRequest {
    List<ManualDrawData> data;

    @Getter
    @Setter
    public static class ManualDrawData {

        private Long sessionId;
        private Long signId;
        private Integer sort;
    }

}

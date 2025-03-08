package com.jsnjwj.common.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasePageRequest extends BaseRequest{

    private Long page;

    private Long limit;

}

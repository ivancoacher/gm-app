package com.jsnjwj.api.aspect;

import com.jsnjwj.user.enums.OperateTypeEnum;

import java.lang.annotation.*;

/**
 * 日志记录的信息
 *
 * @author zhoujian
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MethodLog {

    OperateTypeEnum operType();

    String targetId();

    String remark();

}

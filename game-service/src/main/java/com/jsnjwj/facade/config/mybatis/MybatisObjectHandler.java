package com.jsnjwj.facade.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Log4j2
@Component
public class MybatisObjectHandler implements MetaObjectHandler {

    /**
     * 创建和修改填充当前登录用户id
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // TODO document why this method is empty
    }

    /**
     * 创建和修改填充当前登录用户id
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        // TODO document why this method is empty
    }

}

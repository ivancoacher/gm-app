package com.jsnjwj.api.config;

import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.annotation.MapperScan;

@Configuration
@MapperScan("com.jsnjwj.compare.dao")
public class DbConfig {
}

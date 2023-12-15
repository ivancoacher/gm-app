package com.jsnjwj.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableAsync
@SpringBootApplication(scanBasePackages = {"com.jsnjwj", "com.jsnjwj.api"},
        exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
@MapperScan(basePackages = {"com.jsnjwj.facade.mapper", "com.jsnjwj.user.dao", "com.jsnjwj.trade.dao"})
public class GameApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameApiApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
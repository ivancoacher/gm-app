package com.jsnjwj.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = {"com.jsnjwj.api", "com.jsnjwj.compare"})
@MapperScan(basePackages = "com.jsnjwj.compare.dao")
@EnableAsync
public class ContractApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContractApiApplication.class, args);
    }
}
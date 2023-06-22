package com.jsnjwj.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(scanBasePackages = {"com.jsnjwj.api", "com.jsnjwj.compare"})
public class ContractApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContractApiApplication.class, args);
    }
}
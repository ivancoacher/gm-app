package com.jsnjwj.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication(scanBasePackages = { "com.jsnjwj" },
		exclude = { org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class })
@MapperScan(basePackages = { "com.jsnjwj.compare.dao", "com.jsnjwj.user.dao", "com.jsnjwj.trade.dao" })
@EnableAsync
public class ContractApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContractApiApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
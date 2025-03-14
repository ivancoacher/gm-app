package com.jsnjwj.facade.config.mybatis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {

	@Bean
	public MySqlInjector sqlInjector() {
		return new MySqlInjector();
	}

}

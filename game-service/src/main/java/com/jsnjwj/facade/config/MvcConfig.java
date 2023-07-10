package com.jsnjwj.facade.config;

import com.jsnjwj.facade.constants.GlobalConstant;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler(GlobalConstant.FILE_MAPPING_PATH)
			.addResourceLocations("file:///" + GlobalConstant.FILE_REAL_PATH);
	}

}

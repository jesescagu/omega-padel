package com.omegapadel.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringBootWelcomePage implements WebMvcConfigurer {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("forward:/index.xhtml");
		registry.addViewController("/error").setViewName("forward:/error.xhtml");
		registry.addViewController("/palas").setViewName("forward:/palas.xhtml");
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
	}
}

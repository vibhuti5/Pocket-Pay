package com.pockeypay.business.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {
    @Value("${frontendserver.url}")
    private String frontEndUrl;

    @Value("${frontendlocal.url}")
    private String frontEndLocalUrl;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(frontEndUrl, frontEndLocalUrl)
                .allowedMethods("*")
                .allowedHeaders("*")  // Allow all headers
                .exposedHeaders("Access-Control-Allow-Origin");  // Expose the required header
    }

}
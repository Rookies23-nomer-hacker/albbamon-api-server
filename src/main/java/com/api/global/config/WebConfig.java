package com.api.global.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	registry.addResourceHandler("/uploads/**")
        .addResourceLocations("file:///home/api_root/uploads/");
    	registry.addResourceHandler("/uploads/resume/**") 
        .addResourceLocations("file:///home/api_root/uploads/resume/");
    	registry.addResourceHandler("/uploads/resume/profile/**") 
        .addResourceLocations("file:///home/api_root/uploads/resume/profile/");
    }
}

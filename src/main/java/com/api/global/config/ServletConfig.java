package com.api.global.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.jasper.servlet.JspServlet;

@Configuration
public class ServletConfig {

    @Bean
    public ServletRegistrationBean<JspServlet> jspServletRegistrationBean() {
        ServletRegistrationBean<JspServlet> registration = new ServletRegistrationBean<>(new JspServlet(), "/upload/resume/portfolio/*");
        registration.setLoadOnStartup(1);
        return registration;
    }
}

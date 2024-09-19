package com.sparta.logistics.client.order.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

import static org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter.requestContextFilter;

@Configuration
public class
AppConfig {

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    @Bean
    public FilterRegistrationBean<RequestContextFilter> registrationBean() {
        FilterRegistrationBean<RequestContextFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(requestContextFilter());
        registrationBean.addUrlPatterns("/*"); // 모든 URL 패턴에 적용
        return registrationBean;
    }
}

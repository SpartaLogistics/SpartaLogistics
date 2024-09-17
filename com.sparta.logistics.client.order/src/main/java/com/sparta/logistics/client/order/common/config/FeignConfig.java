package com.sparta.logistics.client.order.common.config;

import feign.Feign;
import feign.hc5.ApacheHttp5Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class FeignConfig {

    @Bean
    public Feign.Builder feignBuilder() {
        return Feign.builder().client(new ApacheHttp5Client());
    }
}
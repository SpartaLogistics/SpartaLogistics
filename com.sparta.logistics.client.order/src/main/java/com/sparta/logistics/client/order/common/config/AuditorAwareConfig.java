package com.sparta.logistics.client.order.common.config;

import com.sparta.logistics.client.order.common.domain.CustomAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
public class AuditorAwareConfig {

    @Bean(name = "customAuditorProvider")
    public AuditorAware<Long> auditorProvider() {
        return new CustomAuditorAware();
    }
}

package com.backend.orderhere.config;

import com.amazonaws.xray.jakarta.servlet.AWSXRayServletFilter;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    @Bean
    public Filter TracingFilter() {
        String appName = "orderhere-" + activeProfile;
        return new AWSXRayServletFilter(appName);
    }

}

package com.NextStepEdu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    // Configuration for multipart file uploads is handled by Spring Boot
    // See application-prod.yaml for multipart settings
}

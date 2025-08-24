package com.complete.api.gateway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@EnableRetry
public class RetryConfig {
    // Retry configuration is handled by Spring Retry annotations and application.yml
}
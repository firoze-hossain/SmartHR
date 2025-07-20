package com.roze.smarthr.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "smart-hr")
@Data
public class SmartHrProperties {
    private String baseUrl;
    // other properties...
}
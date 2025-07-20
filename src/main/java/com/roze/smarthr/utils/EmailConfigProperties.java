package com.roze.smarthr.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "smart-hr.email")
public class EmailConfigProperties {
    private String from;
    private String adminEmail;
    private String passwordResetUrl;
    private String accountActivationUrl;
    private String baseUrl;
}
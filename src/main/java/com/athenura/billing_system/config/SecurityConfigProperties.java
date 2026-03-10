package com.athenura.billing_system.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.security")
public class SecurityConfigProperties {

    private String adminSecret;
    private String managerSecret;
}
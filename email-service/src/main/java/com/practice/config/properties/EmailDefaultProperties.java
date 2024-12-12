package com.practice.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "email.default")
public class EmailDefaultProperties {
    private String defaultFrom;
    private String defaultFromPassword;
}

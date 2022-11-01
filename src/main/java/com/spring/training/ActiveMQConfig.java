package com.spring.training;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.activemq")
@Data
public class ActiveMQConfig {
    String brokerUrl;
    String userName;
    String password;
}

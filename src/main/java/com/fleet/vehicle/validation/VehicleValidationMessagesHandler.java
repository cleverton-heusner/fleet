package com.fleet.vehicle.validation;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "vehicle")
@PropertySource("classpath:ValidationMessages.properties")
@Getter
@Setter
public class VehicleValidationMessagesHandler {

    private String notFound;
    private String chassisInUse;
}

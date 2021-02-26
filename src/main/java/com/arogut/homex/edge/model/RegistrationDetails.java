package com.arogut.homex.edge.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@Component
public class RegistrationDetails {

    private boolean isAuthorized;
    private String token;
    private long expiration;
    private LocalDateTime lastTokenUpdate;
}

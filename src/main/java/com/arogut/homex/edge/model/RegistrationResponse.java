package com.arogut.homex.edge.model;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class RegistrationResponse {

    String deviceId;
    String token;
    long expiration;
}

package com.arogut.homex.edge.model;

import com.arogut.homex.edge.config.properties.EdgeProperties;
import lombok.Builder;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Builder
@Value
public class RegistrationRequest {

    @Valid
    @NotNull(message = "{metadata.cannot.be.null}")
    DeviceMetadata metadata;

    @Valid
    @NotNull(message = "{contract.cannot.be.null}")
    Contract contract;

    public static RegistrationRequest from(EdgeProperties edgeProperties) {
        return RegistrationRequest.builder()
                .metadata(edgeProperties.getDeviceMetadata())
                .contract(edgeProperties.getContract())
                .build();
    }
}

package com.arogut.homex.edge.config.properties;

import com.arogut.homex.edge.model.Contract;
import com.arogut.homex.edge.model.DeviceMetadata;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@RequiredArgsConstructor
@Component
@ConfigurationProperties(prefix = "edge")
public class EdgeProperties {

    private final DeviceMetadata deviceMetadata;
    private final Contract contract;
    private long publishDelay = 10000;
    private long publishPeriod = 5000;
}

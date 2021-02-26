package com.arogut.homex.edge.model;

import com.arogut.homex.edge.Utils;
import lombok.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class DeviceMetadata {

    private String id;

    @NotEmpty
    private String macAddress;

    @NotNull
    private String name;

    @NotEmpty
    private String host;

    @Max(99999)
    private int port;

    @PostConstruct
    public void setUpMacAddress() {
        macAddress = Utils.getMacAddress();
    }
}

package com.arogut.homex.edge.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;


@Value
@Builder
@AllArgsConstructor
public class DeviceMessage {

    String deviceId;
    long measuredTime;
    List<Measurement<?>> data;
}

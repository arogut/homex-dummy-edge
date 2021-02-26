package com.arogut.homex.edge.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Measurement<T> {

    String name;

    T value;
}

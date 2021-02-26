package com.arogut.homex.edge.model;

import javax.validation.constraints.NotNull;

public class CommandParam {

    @NotNull
    private String name;

    @NotNull
    private ValueType type;
}

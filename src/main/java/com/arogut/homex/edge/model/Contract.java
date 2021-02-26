package com.arogut.homex.edge.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Contract {

    @NotNull
    private Set<@Valid Measurement> measurements;

    @NotNull
    private Set<@Valid Command> commands;

    @Data
    @SuperBuilder
    @NoArgsConstructor
    public static class Command {

        @NotNull
        private String name;

        @NotEmpty
        private String endpoint;

        private Set<@Valid CommandParam> params;
    }

    @Data
    @SuperBuilder
    @NoArgsConstructor
    public static class Measurement {

        @NotNull
        private String name;
        @NotNull
        private int min;
        @NotNull
        private int max;
        @NotNull
        private ValueType type;
    }
}

package com.arogut.homex.edge.service.generator;

import com.arogut.homex.edge.model.Contract;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

class GeneratorsTest {

    @Test
    void shouldReturnCorrectGenerator() {
        Assertions.assertThat(Generators.DOUBLE_GENERATOR.get().getClass()).isEqualTo(Generators.DoubleGenerator.class);
    }

    @Test
    void shouldGenerateDoubleFromRange() {
        Contract.Measurement numberMeasurement = Contract.Measurement.builder()
                .min(10)
                .max(15)
                .build();
        IntStream.range(0, 10).forEach(i ->
                Assertions.assertThat((double) Generators.DOUBLE_GENERATOR.get().generateValue(numberMeasurement).getValue())
                        .isBetween((double) numberMeasurement.getMin(), (double) numberMeasurement.getMax())
        );
    }

}

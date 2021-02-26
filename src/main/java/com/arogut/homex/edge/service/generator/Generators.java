package com.arogut.homex.edge.service.generator;

import com.arogut.homex.edge.model.Contract;
import com.arogut.homex.edge.model.Measurement;
import lombok.RequiredArgsConstructor;

import java.util.Random;
import java.util.function.Supplier;

@RequiredArgsConstructor
public enum Generators implements Supplier<Generators.Generator> {
    DoubleGenerator(DoubleGenerator::new);

    private final Supplier<Generator> factory;

    @Override
    public Generator get() {
        return factory.get();
    }

    public interface Generator {
        Measurement<?> generateValue(Contract.Measurement measurement);
    }

    static class DoubleGenerator implements Generator {

        Random r = new Random();

        @Override
        public Measurement<?> generateValue(Contract.Measurement measurement) {
            return Measurement.builder()
                    .name(measurement.getName())
                    .value(measurement.getMin() + (measurement.getMax() - measurement.getMin()) * r.nextDouble())
                    .build();
        }
    }
}

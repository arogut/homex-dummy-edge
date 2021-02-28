package com.arogut.homex.edge.service.generator;

import com.arogut.homex.edge.model.Contract;
import com.arogut.homex.edge.model.Measurement;
import lombok.RequiredArgsConstructor;

import java.util.Random;
import java.util.function.Supplier;

@RequiredArgsConstructor
public enum Generators implements Supplier<Generators.Generator<Double>> {
    DOUBLE_GENERATOR(DoubleGenerator::new);

    @Override
    public Generator<Double> get() {
        return factory.get();
    }

    private final Supplier<Generator<Double>> factory;

    public interface Generator<T> {
        Measurement<T> generateValue(Contract.Measurement measurement);
    }

    static class DoubleGenerator implements Generator<Double> {

        Random r = new Random();

        @Override
        public Measurement<Double> generateValue(Contract.Measurement measurement) {
            return Measurement.<Double>builder()
                    .name(measurement.getName())
                    .value(measurement.getMin() + (measurement.getMax() - measurement.getMin()) * r.nextDouble())
                    .build();
        }
    }
}

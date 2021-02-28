package com.arogut.homex.edge.service.generator;

import com.arogut.homex.edge.model.Contract;
import com.arogut.homex.edge.model.Measurement;
import lombok.RequiredArgsConstructor;

import java.util.Random;
import java.util.function.Supplier;

@RequiredArgsConstructor
public enum Generators {

    DOUBLE_GENERATOR {

        final Random r = new Random();

        @Override
        public Measurement<Double> generateValue(Contract.Measurement measurement) {
            return Measurement.<Double>builder()
                    .name(measurement.getName())
                    .value(measurement.getMin() + (measurement.getMax() - measurement.getMin()) * r.nextDouble())
                    .build();
        }
    };

    public abstract <T> Measurement<T> generateValue(Contract.Measurement measurement);
}

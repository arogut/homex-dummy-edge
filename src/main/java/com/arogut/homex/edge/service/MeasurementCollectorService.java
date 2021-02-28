package com.arogut.homex.edge.service;

import com.arogut.homex.edge.config.properties.EdgeProperties;
import com.arogut.homex.edge.model.Contract;
import com.arogut.homex.edge.model.Measurement;
import com.arogut.homex.edge.service.generator.Generators;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeasurementCollectorService {

    private final EdgeProperties edgeProperties;

    public List<Measurement<Double>> getMeasurement() {
        return edgeProperties.getContract().getMeasurements()
                .stream()
                .map(this::generateValue)
                .collect(Collectors.toList());
    }

    private Measurement<Double> generateValue(Contract.Measurement measurement) {
        return Generators.DOUBLE_GENERATOR
                .generateValue(measurement);
    }

}

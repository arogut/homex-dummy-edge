package com.arogut.homex.edge.service;

import com.arogut.homex.edge.config.properties.EdgeProperties;
import com.arogut.homex.edge.model.Contract;
import com.arogut.homex.edge.model.DeviceMetadata;
import com.arogut.homex.edge.model.Measurement;
import com.arogut.homex.edge.model.RegistrationDetails;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class MeasurementCollectorServiceTest {

    private final EdgeProperties edgeProperties = new EdgeProperties(new DeviceMetadata(), new Contract());

    private final RegistrationDetails registrationDetails = new RegistrationDetails();

    @Test
    void shouldReturnListOfGeneratedMeasurements() {
        Contract.Measurement temp = Contract.Measurement.builder()
                .name("temperature")
                .min(10)
                .max(15)
                .build();
        Contract.Measurement hum = Contract.Measurement.builder()
                .name("humidity")
                .min(0)
                .max(100)
                .build();
        edgeProperties.getContract().setMeasurements(Set.of(temp, hum));
        MeasurementCollectorService collectorService = new MeasurementCollectorService(edgeProperties);

        List<Measurement<Double>> contractMeasurements = collectorService.getMeasurement();

        Assertions.assertThat(contractMeasurements)
                .extracting("name")
                .containsExactlyInAnyOrder(
                        temp.getName(),
                        hum.getName()
                );
        Assertions.assertThat(contractMeasurements).extracting("value").doesNotContainNull();
    }
}

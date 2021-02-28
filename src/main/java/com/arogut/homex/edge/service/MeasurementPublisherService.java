package com.arogut.homex.edge.service;

import com.arogut.homex.edge.config.properties.EdgeProperties;
import com.arogut.homex.edge.model.DeviceMessage;
import com.arogut.homex.edge.model.RegistrationDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class MeasurementPublisherService {

    private final EdgeProperties edgeProperties;
    private final MeasurementCollectorService collectorService;
    private final GatewayService gatewayService;
    private final RegistrationDetails registrationDetails;

    @PostConstruct
    public void init() {
        measurementsFlow().subscribe();
    }

    public Flux<String> measurementsFlow() {
        return Flux.interval(Duration.ofMillis(edgeProperties.getPublishDelay()), Duration.ofMillis(edgeProperties.getPublishPeriod()))
                .filter(l -> registrationDetails.isAuthorized())
                .map(l -> collectorService.getMeasurement())
                .map(m -> DeviceMessage.<Double>builder()
                        .deviceId(edgeProperties.getDeviceMetadata().getId())
                        .measuredTime(System.currentTimeMillis())
                        .data(m)
                        .build())
                .flatMap(gatewayService::sendMessage)
                .onErrorContinue((e, o) -> log.warn("Unable to send measurements: {}", e.getMessage(), e));
    }
}

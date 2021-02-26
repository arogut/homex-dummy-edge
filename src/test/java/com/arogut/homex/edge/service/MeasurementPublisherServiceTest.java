package com.arogut.homex.edge.service;

import com.arogut.homex.edge.config.properties.EdgeProperties;
import com.arogut.homex.edge.model.Contract;
import com.arogut.homex.edge.model.DeviceMessage;
import com.arogut.homex.edge.model.DeviceMetadata;
import com.arogut.homex.edge.model.RegistrationDetails;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class MeasurementPublisherServiceTest {

    private final EdgeProperties edgeProperties = new EdgeProperties(new DeviceMetadata(), new Contract());

    private final RegistrationDetails registrationDetails = new RegistrationDetails();

    @Mock
    private MeasurementCollectorService collectorService;

    @Mock
    private GatewayService gatewayService;

    private MeasurementPublisherService publisherService;

    @BeforeEach
    void setUp() {
        publisherService = new MeasurementPublisherService(edgeProperties, collectorService,
                gatewayService, registrationDetails);
    }

    @Test
    void shouldProperlyEmitPeriodicEventsAndSendDeviceMessages() {
        String uuid = UUID.randomUUID().toString();
        edgeProperties.setPublishDelay(1000);
        edgeProperties.setPublishPeriod(5000);
        edgeProperties.getDeviceMetadata().setId(uuid);
        registrationDetails.setAuthorized(true);

        Mockito.when(gatewayService.sendMessage(Mockito.any(DeviceMessage.class))).thenReturn(Mono.just("1"), Mono.just("2"));

        StepVerifier.withVirtualTime(() -> publisherService.measurementsFlow())
                .expectSubscription()
                .expectNoEvent(Duration.ofMillis(edgeProperties.getPublishDelay()))
                .consumeNextWith(measurements -> {
                    assertNext(1, uuid, measurements);
                })
                .expectNoEvent(Duration.ofMillis(edgeProperties.getPublishPeriod()))
                .consumeNextWith(measurements -> {
                    assertNext(2, uuid, "2");
                })
                .thenCancel()
                .verify();
    }

    private void assertNext(int order, String deviceId, String returnedValue) {
        ArgumentCaptor<DeviceMessage> deviceMessageCaptor = ArgumentCaptor.forClass(DeviceMessage.class);
        Mockito.verify(collectorService, Mockito.times(order)).getMeasurement();
        Mockito.verify(gatewayService, Mockito.times(order)).sendMessage(deviceMessageCaptor.capture());
        DeviceMessage sentMessage = deviceMessageCaptor.getValue();
        Assertions.assertAll(
                () -> Assertions.assertEquals(deviceId, sentMessage.getDeviceId()),
                () -> Assertions.assertEquals(returnedValue, String.valueOf(order))
        );
    }
}

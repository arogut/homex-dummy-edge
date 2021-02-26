package com.arogut.homex.edge.service;

import com.arogut.homex.edge.config.properties.EdgeProperties;
import com.arogut.homex.edge.model.Contract;
import com.arogut.homex.edge.model.DeviceMetadata;
import com.arogut.homex.edge.model.RegistrationDetails;
import com.arogut.homex.edge.model.RegistrationRequest;
import com.arogut.homex.edge.model.RegistrationResponse;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.TemporalUnitWithinOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Mock
    private GatewayService gatewayService;

    private RegistrationDetails registrationDetails;
    private EdgeProperties edgeProperties;
    private RegistrationService registrationService;

    @BeforeEach
    void setUp() {
        registrationDetails = new RegistrationDetails();
        edgeProperties = buildEdgeProperties();
        registrationService = new RegistrationService(registrationDetails, gatewayService, edgeProperties);
    }

    @Test
    void shouldSuccessfullyRegister() {
        RegistrationRequest registrationRequest = RegistrationRequest.from(edgeProperties);
        RegistrationResponse registrationResponse = buildRegistrationResponse();
        Mockito.when(gatewayService.register(registrationRequest)).thenReturn(Mono.just(registrationResponse));

        StepVerifier.withVirtualTime(() -> registrationService.register())
                .expectSubscription()
                .assertNext(measurements -> {
                    Mockito.verify(gatewayService, Mockito.times(1)).register(registrationRequest);
                    assertAuth();
                })
                .expectComplete()
                .verify();
    }

    @Test
    void shouldSuccessfullyScheduleTokenRefresh() {
        RegistrationResponse registrationResponse = buildRegistrationResponse();
        Mockito.when(gatewayService.refresh()).thenReturn(Mono.just(registrationResponse));

        StepVerifier.withVirtualTime(() -> registrationService.scheduledRefresh(Duration.ofMillis(800)))
                .expectSubscription()
                .expectNoEvent(Duration.ofMillis(800))
                .assertNext(measurements -> {
                    Mockito.verify(gatewayService, Mockito.times(1)).refresh();
                    assertAuth();
                })
                .expectNoEvent(Duration.ofMillis(800))
                .assertNext(measurements -> {
                    Mockito.verify(gatewayService, Mockito.times(2)).refresh();
                    assertAuth();
                })
                .thenCancel()
                .verify();
    }

    private void assertAuth() {
        Assertions.assertThat(edgeProperties.getDeviceMetadata().getId()).isEqualTo("dummy-device");
        Assertions.assertThat(registrationDetails.isAuthorized()).isTrue();
        Assertions.assertThat(registrationDetails.getToken()).isEqualTo("real-token");
        Assertions.assertThat(registrationDetails.getExpiration()).isEqualTo(1000);
        Assertions.assertThat(registrationDetails.getLastTokenUpdate())
                .isCloseTo(LocalDateTime.now(), new TemporalUnitWithinOffset(300, ChronoUnit.MILLIS));
    }

    private EdgeProperties buildEdgeProperties() {
        DeviceMetadata deviceMetadata = DeviceMetadata.builder()
                .name("dummy-device-name")
                .host("local")
                .port(8909)
                .build();

        return new EdgeProperties(deviceMetadata, new Contract());
    }

    private RegistrationResponse buildRegistrationResponse() {
        return RegistrationResponse.builder()
                .deviceId("dummy-device")
                .expiration(1000L)
                .token("real-token")
                .build();
    }
}

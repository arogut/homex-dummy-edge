package com.arogut.homex.edge.service;

import com.arogut.homex.edge.config.properties.EdgeProperties;
import com.arogut.homex.edge.model.RegistrationDetails;
import com.arogut.homex.edge.model.RegistrationRequest;
import com.arogut.homex.edge.model.RegistrationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class RegistrationService {

    private final RegistrationDetails registrationDetails;
    private final GatewayService gatewayService;
    private final EdgeProperties edgeProperties;

    @PostConstruct
    public void setUp() {
        register()
                .flatMap(response -> scheduledRefresh(Duration.ofMillis(response.getExpiration() * 8 / 10)))
                .subscribe();
    }

    public Flux<RegistrationResponse> register() {
        return Flux.from(gatewayService.register(RegistrationRequest.from(edgeProperties)))
                .retryWhen(Retry.backoff(5, Duration.ofMillis(1000)))
                .doOnNext(response -> {
                    updateRegistrationDetails(response);
                    log.info("Registered with id: {}", response.getDeviceId());
                });
    }

    public Flux<RegistrationResponse> scheduledRefresh(Duration duration) {
        return Flux.interval(duration, duration)
                .flatMap(i -> gatewayService.refresh())
                .doOnNext(this::updateRegistrationDetails);
    }

    private void updateRegistrationDetails(RegistrationResponse response) {
        edgeProperties.getDeviceMetadata().setId(response.getDeviceId());
        registrationDetails.setAuthorized(true);
        registrationDetails.setToken(response.getToken());
        registrationDetails.setExpiration(response.getExpiration());
        registrationDetails.setLastTokenUpdate(LocalDateTime.now());
    }
}

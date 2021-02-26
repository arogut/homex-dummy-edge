package com.arogut.homex.edge.client;

import com.arogut.homex.edge.model.RegistrationRequest;
import com.arogut.homex.edge.model.RegistrationResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(name = "GatewayClient", url = "${gateway.url}")
public interface GatewayClient {

    @PostMapping("/devices/auth")
    Mono<RegistrationResponse> register(@RequestBody RegistrationRequest registrationRequest);
}

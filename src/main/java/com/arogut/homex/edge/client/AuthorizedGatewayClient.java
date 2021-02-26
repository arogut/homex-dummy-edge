package com.arogut.homex.edge.client;

import com.arogut.homex.edge.model.DeviceMessage;
import com.arogut.homex.edge.model.RegistrationResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(name = "AuthorizedGatewayClient", url = "${gateway.url}", configuration = FeignClientJwtInterceptor.class)
public interface AuthorizedGatewayClient {

    @PostMapping("/devices/{id}/measurement")
    Mono<String> sendMessage(@RequestBody DeviceMessage message, @PathVariable String id);

    @GetMapping("/devices/auth/refresh")
    Mono<RegistrationResponse> refresh();
}

package com.arogut.homex.edge.service;

import com.arogut.homex.edge.client.AuthorizedGatewayClient;
import com.arogut.homex.edge.client.GatewayClient;
import com.arogut.homex.edge.model.DeviceMessage;
import com.arogut.homex.edge.model.RegistrationRequest;
import com.arogut.homex.edge.model.RegistrationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GatewayService {

    private final GatewayClient gatewayClient;
    private final AuthorizedGatewayClient authorizedGatewayClient;

    public Mono<RegistrationResponse> register(RegistrationRequest registrationRequest) {
        return gatewayClient.register(registrationRequest);
    }

    public Mono<RegistrationResponse> refresh() {
        return authorizedGatewayClient.refresh();
    }

    public Mono<String> sendMessage(DeviceMessage<?> message) {
        return authorizedGatewayClient.sendMessage(message, message.getDeviceId());
    }
}

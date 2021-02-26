package com.arogut.homex.edge.client;

import com.arogut.homex.edge.model.RegistrationDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactivefeign.client.ReactiveHttpRequest;
import reactivefeign.client.ReactiveHttpRequestInterceptor;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FeignClientJwtInterceptor implements ReactiveHttpRequestInterceptor {

    private final RegistrationDetails registrationDetails;

    @Override
    public Mono<ReactiveHttpRequest> apply(ReactiveHttpRequest reactiveHttpRequest) {
        reactiveHttpRequest.headers().put("Authorization", List.of("Bearer " + registrationDetails.getToken()));
        return Mono.just(reactiveHttpRequest);
    }
}

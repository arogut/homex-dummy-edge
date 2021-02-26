package com.arogut.homex.edge.client;

import com.arogut.homex.edge.model.RegistrationDetails;
import feign.MethodMetadata;
import feign.Target;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import reactivefeign.client.ReactiveHttpRequest;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class FeignClientJwtInterceptorTest {

    @Test
    void shouldProperlyAddAuthHeader() throws Exception {
        RegistrationDetails registrationDetails = new RegistrationDetails();
        registrationDetails.setToken("valid-token");
        FeignClientJwtInterceptor interceptor = new FeignClientJwtInterceptor(registrationDetails);
        ReactiveHttpRequest reactiveHttpRequest = new ReactiveHttpRequest(createMethodMetadata(), Target.EmptyTarget.create(Void.class), URI.create(""), new HashMap<>(), null);

        interceptor.apply(reactiveHttpRequest);

        Assertions.assertThat(reactiveHttpRequest.headers()).containsKey("Authorization");
        Assertions.assertThat(reactiveHttpRequest.headers()).containsValue(List.of("Bearer valid-token"));
    }

    private MethodMetadata createMethodMetadata() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<MethodMetadata> constructor = MethodMetadata.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance(null);
    }
}

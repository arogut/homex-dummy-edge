package com.arogut.homex.edge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactivefeign.spring.config.EnableReactiveFeignClients;

@SpringBootApplication
@EnableReactiveFeignClients
public class HomexEdgeApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomexEdgeApplication.class, args);
    }
}

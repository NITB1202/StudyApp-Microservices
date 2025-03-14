package com.study.apigateway.configs;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfig {
    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()

                .route("user-service", r -> r.path("/api/users/**")
                        .filters(f -> f.rewritePath("/user-service/(?<segment>.*)", "/${segment}"))
                        .uri("lb://user-service"))

                .build();
    }
}

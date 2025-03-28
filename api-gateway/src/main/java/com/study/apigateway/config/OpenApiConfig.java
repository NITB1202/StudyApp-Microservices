package com.study.apigateway.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPi(){
        return new OpenAPI()
                .info(new Info()
                        .title("StudyPal API")
                        .version("1.0")
                        .description("API documentation for the self-study mobile application using WebFlux & gRPC"));
    }
}
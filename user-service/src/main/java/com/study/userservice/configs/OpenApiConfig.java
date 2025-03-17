package com.study.userservice.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPi(){
        return new OpenAPI()
                .servers(Collections.singletonList(new Server().url("http://localhost:8080/user-service")))
                .info(new Info()
                        .title("User Service API")
                        .version("1.0")
                        .description("API documentation for the User Service."));
    }
}

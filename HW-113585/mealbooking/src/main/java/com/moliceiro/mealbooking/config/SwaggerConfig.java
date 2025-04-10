package com.moliceiro.mealbooking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Meal Booking API")
                        .version("1.0.0")
                        .description("API documentation for the Meal Booking application, providing endpoints for meal reservations and cache statistics."));
    }

    @Bean
    public GroupedOpenApi problemDomainApi() {
        return GroupedOpenApi.builder()
                .group("Problem Domain")
                .pathsToMatch("/menus", "/book", "/check", "/cancel", "/verify")
                .build();
    }
}
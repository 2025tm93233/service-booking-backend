package com.servicebooking.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI serviceBookingOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Service Booking API")
                        .description("RESTful API for Service Booking Platform - A microservices application for booking local services like plumbing, electrical, tutoring, cleaning, gardening, and moving services.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Service Booking Team")
                                .email("support@servicebooking.com")
                                .url("https://servicebooking.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://choosealicense.com/licenses/mit/")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Development Server"),
                        new Server().url("https://api.servicebooking.com").description("Production Server")
                ))
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT authentication token. Format: Bearer {token}")))
                .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"));
    }
}

package com.smart_delivery_platform.order_service.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI orderServiceOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Smart Delivery Order Service API")
                        .description("API documentation for creating orders, retrieving order details, and retrying payments.")
                        .version("v1")
                        .contact(new Contact()
                                .name("Smart Delivery Platform")
                                .email("support@smart-delivery-platform.com"))
                        .license(new License()
                                .name("Internal Use")));
    }
}

package com.analytics.web.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition
@Configuration
@Slf4j
public class SwaggerConfig {
    @Bean
    public OpenAPI baseOpenAPI() {
    log.info("Swagger configuration has been successfully initialized.");
    
    return new OpenAPI()
        .info(
            new Info()
                .title("Analytics")
                .version("1.0.0")
                .description("Swagger configuration for analytics application")
                .contact(new Contact().email("adriangherasim1@gmail.com").name("Gherasim Daniel Adrian")));
    }
}

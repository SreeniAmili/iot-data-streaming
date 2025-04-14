package com.relay42.iot.stream.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * Configuration class for setting up Swagger/OpenAPI documentation for the IoT Data Streaming API.
 */

@Configuration
public class SwaggerConfig {

    /**
     * Creates and configures an OpenAPI bean to define the API documentation details.
     *
     * @return an OpenAPI instance with metadata about the IoT Data Streaming API.
     */
    @Bean
    public OpenAPI iotApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("IoT Data Streaming API")
                        .description("Stream and query real-time sensor data")
                        .version("1.0"));
    }

    /**
     * Creates and configures a GroupedOpenApi bean for version 1 of the API.
     * Specifies the package to scan for controllers and the path patterns to include in the API group.
     *
     * @return a GroupedOpenApi instance for version 1 of the API.
     */
    @Bean
    public GroupedOpenApi v1Api() {
        return GroupedOpenApi.builder()
                .group("v1")
                .packagesToScan("com.relay42.iot.stream.controller")
                .pathsToMatch("/api/v1/**")
                .build();
    }
}

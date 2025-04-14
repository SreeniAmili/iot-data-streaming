package com.relay42.iot.stream.simulator;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.relay42.iot.stream.model.SensorRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Component class responsible for simulating sensor data and sending it to the IoT Data Streaming API.
 * Uses scheduled tasks to generate random sensor data at fixed intervals and sends it to the API endpoint.
 * This is useful for testing and validating the data ingestion pipeline.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SensorSimulator {

    private final SimulatorConfig config;
    private final RestTemplate restTemplate = new RestTemplateBuilder()
            .basicAuthentication("admin", "password") // Replace with config properties if needed
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private static final String INGEST_URL = "http://localhost:8081/api/v1/sensors/data";

    private final Random random = new Random();
    private final AtomicInteger recordCount = new AtomicInteger(0); // Initialize recordCount

    private double random(double min, double max) {
        return min + (max - min) * random.nextDouble();
    }

    /**
     * Simulates sensor data by generating random values for different devices and metrics.
     * Sends the simulated data to the IoT Data Streaming API at fixed intervals.
     * The simulation behavior is controlled by the configuration properties:
     * - `simulator.enabled`: Enables or disables the simulation.
     * - `simulator.fixed-rate-ms`: Defines the interval (in milliseconds) between simulation runs.
     * - `simulator.max-records`: Limits the total number of records sent during the simulation.
     * - `simulator.allow-spikes`: Allows generating random spikes in the data if enabled.
     *
     * The method stops sending data once the maximum record limit is reached.
     */
    @Scheduled(fixedRateString = "#{@simulatorConfig.fixedRateMs}")
    public void simulate() {
        if (!config.isEnabled()) {
            log.info("Simulation is disabled.");
            return;
        }

        if (recordCount.get() >= config.getMaxRecords()) {
            log.info("Maximum record limit of {} reached. Stopping simulation.", config.getMaxRecords());
            return;
        }

        List<SensorRequest> simulated = List.of(
                new SensorRequest("thermostat-1", "temperature", random(20, 30), LocalDateTime.now(), "thermostat"),
                new SensorRequest("heartrate-1", "heart-rate", random(60, 100), LocalDateTime.now(), "wearable"),
                new SensorRequest("car-ecu-1", "fuel-level", random(10, 80), LocalDateTime.now(), "vehicle")
        );

        simulated.stream()
                .limit(config.getMaxRecords() - recordCount.get())
                .forEach(data -> {
                    try {
                        ResponseEntity<String> response = restTemplate.postForEntity(INGEST_URL, data, String.class);
                        log.info("Data sent: {}, Response: {}", objectMapper.writeValueAsString(data), response.getStatusCode());
                        recordCount.incrementAndGet();
                    } catch (Exception e) {
                        log.error("Failed to send data: {}", e.getMessage());
                    }
                });
    }
}


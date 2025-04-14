package com.relay42.iot.stream.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.relay42.iot.stream.entity.SensorData;
import com.relay42.iot.stream.model.SensorRequest;
import com.relay42.iot.stream.repository.SensorDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class SensorDataControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private SensorDataRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        repository.deleteAll(); // Clean up the database before each test
    }

    @Test
    void testIngestSensorData() throws Exception {

        SensorRequest request = SensorRequest.builder()
                .deviceId("device-1")
                .metric("temperature")
                .metricValue(25.5)
                .dataTimestamp(LocalDateTime.now())
                .build();


        mockMvc.perform(post("/api/v1/sensors/data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

         assertEquals(1, repository.count());
    }

    @Test
    void testGetSensorDataStats() throws Exception {

        SensorData data1 = SensorData.builder()
                .deviceId("device-1")
                .metric("temperature")
                .metricValue(25.5)
                .dataTimestamp(LocalDateTime.now())
                .build();
        SensorData data2 = SensorData.builder()
                .deviceId("device-1")
                .metric("temperature")
                .metricValue(26.0)
                .dataTimestamp(LocalDateTime.now())
                .build();
        repository.save(data1);
        repository.save(data2);


        mockMvc.perform(get("/api/v1/sensors/data/stats")
                        .param("deviceId", "device-1")
                        .param("metric", "temperature"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.min", is(25.5)))
                .andExpect(jsonPath("$.data.max", is(26.0)))
                .andExpect(jsonPath("$.data.avg", is(25.75)))
                .andExpect(jsonPath("$.data.median", is(25.75)));
    }

    @Test
    void testGetSensorDataStatsByTimeRange() throws Exception {

        LocalDateTime now = LocalDateTime.now();
        SensorData data1 = SensorData.builder()
                .deviceId("device-1")
                .metric("temperature")
                .metricValue(25.5)
                .dataTimestamp(now.minusHours(2))
                .build();
        SensorData data2 = SensorData.builder()
                .deviceId("device-1")
                .metric("temperature")
                .metricValue(26.0)
                .dataTimestamp(now.minusHours(1))
                .build();
        repository.save(data1);
        repository.save(data2);


        mockMvc.perform(get("/api/v1/sensors/data/stats/range")
                        .param("deviceId", "device-1")
                        .param("metric", "temperature")
                        .param("from", now.minusHours(3).toString())
                        .param("to", now.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.min", is(25.5)))
                .andExpect(jsonPath("$.data.max", is(26.0)))
                .andExpect(jsonPath("$.data.avg", is(25.75)))
                .andExpect(jsonPath("$.data.median", is(25.75)));
    }

    @Test
    void testGetSensorDataStatsNoContent() throws Exception {

        mockMvc.perform(get("/api/v1/sensors/data/stats")
                        .param("deviceId", "device-1")
                        .param("metric", "temperature"))
                .andExpect(status().isNoContent());
    }
}
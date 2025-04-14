package com.relay42.iot.stream.controller;

import com.relay42.iot.stream.aggregation.AggregationStrategy;
import com.relay42.iot.stream.aggregation.AggregationStrategyFactory;
import com.relay42.iot.stream.entity.SensorData;
import com.relay42.iot.stream.mapper.SensorDataMapper;
import com.relay42.iot.stream.model.MetricSummary;
import com.relay42.iot.stream.model.SensorRequest;
import com.relay42.iot.stream.model.SensorResponse;
import com.relay42.iot.stream.service.SensorDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SensorDataControllerV1Test {

    @Mock
    private SensorDataService service;

    @Mock
    private AggregationStrategyFactory strategyFactory;

    @Mock
    private SensorDataMapper mapper;

    @Mock
    private AggregationStrategy aggregationStrategy;

    @InjectMocks
    private SensorDataControllerV1 controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIngestSensorData() {

        SensorRequest request = SensorRequest.builder()
                .deviceId("device-1")
                .metric("temperature")
                .metricValue(25.5)
                .dataTimestamp(LocalDateTime.now())
                .build();

        SensorData sensorData = SensorData.builder()
                .deviceId("device-1")
                .metric("temperature")
                .metricValue(25.5)
                .dataTimestamp(request.getDataTimestamp())
                .build();

        when(mapper.toEntity(request)).thenReturn(sensorData);


        ResponseEntity<Void> response = controller.ingestSensorData(request);


        assertEquals(201, response.getStatusCode().value());
        verify(service, times(1)).saveSensorData(sensorData);
    }

    @Test
    void testGetSensorDataStats() {

        String deviceId = "device-1";
        String metric = "temperature";
        List<SensorData> mockData = List.of(
                SensorData.builder().deviceId(deviceId).metric(metric).metricValue(25.5).build(),
                SensorData.builder().deviceId(deviceId).metric(metric).metricValue(26.0).build()
        );
        MetricSummary result = MetricSummary.builder()
                .metric(metric)
                .min(25.5)
                .max(26.0)
                .avg(25.75)
                .median(25.75)
                .build();

        when(service.getSensorData(deviceId, metric)).thenReturn(mockData);
        when(strategyFactory.getStrategy(metric)).thenReturn(aggregationStrategy);
        when(aggregationStrategy.aggregateMetrics(metric, mockData)).thenReturn(result);


        ResponseEntity<SensorResponse<MetricSummary>> response = controller.getSensorDataStats(deviceId, metric);


        assertEquals(200, response.getStatusCode().value());
        assertEquals(result, response.getBody().getData());
        verify(service, times(1)).getSensorData(deviceId, metric);
    }

    @Test
    void testGetSensorDataStatsByTimeRange() {

        String deviceId = "device-1";
        String metric = "temperature";
        LocalDateTime from = LocalDateTime.now().minusDays(1);
        LocalDateTime to = LocalDateTime.now();
        List<SensorData> mockData = List.of(
                SensorData.builder().deviceId(deviceId).metric(metric).metricValue(25.5).dataTimestamp(from.plusHours(1)).build(),
                SensorData.builder().deviceId(deviceId).metric(metric).metricValue(26.0).dataTimestamp(from.plusHours(2)).build()
        );
        MetricSummary result = MetricSummary.builder()
                .metric(metric)
                .min(25.5)
                .max(26.0)
                .avg(25.75)
                .median(25.75)
                .build();

        when(service.getSensorData(deviceId, metric)).thenReturn(mockData);
        when(strategyFactory.getStrategy(metric)).thenReturn(aggregationStrategy);
        when(aggregationStrategy.aggregateMetrics(metric, mockData)).thenReturn(result);


        ResponseEntity<SensorResponse<MetricSummary>> response = controller.getSensorDataStatsByTimeRange(deviceId, metric, from, to);


        assertEquals(200, response.getStatusCode().value());
        assertEquals(result, response.getBody().getData());
        verify(service, times(1)).getSensorData(deviceId, metric);
    }
}
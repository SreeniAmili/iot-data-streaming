package com.relay42.iot.stream.service;

import com.relay42.iot.stream.entity.SensorData;
import com.relay42.iot.stream.repository.SensorDataRepository;
import com.relay42.iot.stream.service.impl.SensorDataServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class SensorDataServiceImplTest {

    @Mock
    private SensorDataRepository repository;

    @InjectMocks
    private SensorDataServiceImpl service;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveSensorData() {

        SensorData sensorData = SensorData.builder()
                .deviceId("device-1")
                .metric("temperature")
                .metricValue(25.5)
                .build();


        service.saveSensorData(sensorData);


        verify(repository, times(1)).save(sensorData);
    }

    @Test
    void testGetSensorData() {

        String deviceId = "device-1";
        String metric = "temperature";
        List<SensorData> mockData = List.of(
                SensorData.builder().deviceId(deviceId).metric(metric).metricValue(25.5).build(),
                SensorData.builder().deviceId(deviceId).metric(metric).metricValue(26.0).build()
        );
        when(repository.findByDeviceIdAndMetric(deviceId, metric)).thenReturn(mockData);


        List<SensorData> result = service.getSensorData(deviceId, metric);


        assertEquals(2, result.size());
        assertEquals(25.5, result.get(0).getMetricValue());
        assertEquals(26.0, result.get(1).getMetricValue());
        verify(repository, times(1)).findByDeviceIdAndMetric(deviceId, metric);
    }

    @Test
    void testGetSensorDataEmptyResult() {

        String deviceId = "device-1";
        String metric = "humidity";
        when(repository.findByDeviceIdAndMetric(deviceId, metric)).thenReturn(List.of());


        List<SensorData> result = service.getSensorData(deviceId, metric);


        assertTrue(result.isEmpty());
        verify(repository, times(1)).findByDeviceIdAndMetric(deviceId, metric);
    }
}
package com.relay42.iot.stream.service.impl;

import com.relay42.iot.stream.entity.SensorData;
import com.relay42.iot.stream.repository.SensorDataRepository;
import com.relay42.iot.stream.service.SensorDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


/**
 * Service implementation class for managing sensor data operations.
 * Provides methods for saving sensor data and retrieving it based on specific criteria.
 * Implements the SensorDataService interface.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SensorDataServiceImpl implements SensorDataService {

    private final SensorDataRepository repository;

    /**
     * Saves a SensorData entity to the database.
     * Logs the entry and exit of the method for debugging purposes.
     *
     * @param data the SensorData entity to be saved.
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSensorData(SensorData data) {
        log.info("Entered into method::saveSensorData");

        repository.save(data);

        log.info("Exit from into method::saveSensorData");
    }

    /**
     * Retrieves a list of SensorData entities for a specific device and metric.
     *
     * @param deviceId the identifier of the device.
     * @param metric the metric type (e.g., temperature, humidity).
     * @return a list of SensorData entities matching the specified device and metric.
     */
    @Override
    @Transactional(readOnly = true)
    public List<SensorData> getSensorData(String deviceId, String metric) {
        return repository.findByDeviceIdAndMetric(deviceId, metric);
    }

    @Override
    public List<SensorData> getDataInRange(String deviceId, String metric, LocalDateTime from, LocalDateTime to) {
        return repository.findByDeviceIdAndMetricAndTimestampRange(deviceId, metric, from, to);
    }
}

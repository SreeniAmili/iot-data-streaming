package com.relay42.iot.stream.service;

import com.relay42.iot.stream.entity.SensorData;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service interface for managing sensor data operations.
 * Provides methods for saving sensor data and retrieving it based on specific criteria.
 */
public interface SensorDataService {

    /**
     * Saves a SensorData entity to the database.
     *
     * @param data the SensorData entity to be saved.
     */
    void saveSensorData(SensorData data);

    /**
     * Retrieves a list of SensorData entities for a specific device and metric.
     *
     * @param deviceId the identifier of the device.
     * @param metric the metric type (e.g., temperature, humidity).
     * @return a list of SensorData entities matching the specified device and metric.
     */
    List<SensorData> getSensorData(String deviceId, String metric);

    List<SensorData> getDataInRange(String deviceId, String metric, LocalDateTime from, LocalDateTime to);
}

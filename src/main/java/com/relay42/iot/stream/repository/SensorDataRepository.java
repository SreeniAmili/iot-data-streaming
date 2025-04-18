package com.relay42.iot.stream.repository;

import com.relay42.iot.stream.entity.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for performing CRUD operations on SensorData entities.
 * Extends JpaRepository to provide built-in methods for database interactions.
 * Includes a custom query method for retrieving sensor data by device ID and metric.
 */
@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Long> {

    /**
     * Retrieves a list of SensorData entities for a specific device and metric.
     * Queries the database using the device ID and metric type as filters.
     *
     * @param deviceId the identifier of the device.
     * @param metric the metric type (e.g., temperature, humidity).
     * @return a list of SensorData entities matching the specified device ID and metric.
     */
    List<SensorData> findByDeviceIdAndMetric(String deviceId, String metric);

    @Query("SELECT s FROM SensorData s WHERE s.deviceId = :deviceId AND s.metric = :metric " +
            "AND s.dataTimestamp BETWEEN :from AND :to")
    List<SensorData> findByDeviceIdAndMetricAndTimestampRange(
            @Param("deviceId") String deviceId,
            @Param("metric") String metric,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
}

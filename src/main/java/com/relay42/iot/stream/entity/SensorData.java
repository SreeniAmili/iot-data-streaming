package com.relay42.iot.stream.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entity class representing sensor data collected from IoT devices.
 * Maps to the 'sensor_data' table in the database.
 */
@Entity
@Table(name = "sensor_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SensorData {

    /**
     * Unique identifier for the sensor data record.
     * Auto-generated by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Identifier of the device that generated the sensor data.
     * Cannot be null.
     */
    @Column(name = "device_id", nullable = false)
    private String deviceId;

    /**
     * Metric type of the sensor data (e.g., temperature, humidity).
     * Cannot be null.
     */
    @Column(nullable = false)
    private String metric;

    /**
     * Value of the metric recorded by the sensor.
     * Cannot be null.
     */
    @Column(name = "metric_value", nullable = false)
    private Double metricValue;

    /**
     * Timestamp when the sensor data was recorded.
     * Cannot be null.
     */
    @Column(name = "data_timestamp", nullable = false)
    private LocalDateTime dataTimestamp;

    /**
     * Type of the device that generated the sensor data (e.g., thermostat, sensor).
     * Optional field.
     */
    @Column(name = "device_type")
    private String deviceType;

}

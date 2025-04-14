package com.relay42.iot.stream.controller;

import com.relay42.iot.stream.aggregation.AggregationStrategyFactory;
import com.relay42.iot.stream.entity.SensorData;
import com.relay42.iot.stream.mapper.SensorDataMapper;
import com.relay42.iot.stream.model.MetricSummary;
import com.relay42.iot.stream.model.SensorRequest;
import com.relay42.iot.stream.model.SensorResponse;
import com.relay42.iot.stream.service.SensorDataService;
import com.relay42.iot.stream.util.TimeFilter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for managing sensor data in the IoT Data Streaming API.
 * Provides endpoints for ingesting sensor data, retrieving aggregated statistics,
 * and filtering data by time range.
 * Utilize services, mappers, and aggregation strategies to handle requests.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/sensors")
@RequiredArgsConstructor
@Tag(name = "Sensor API", description = "Sensor readings and aggregation endpoints")
@Validated
public class SensorDataControllerV1 {

    private final SensorDataService service;
    private final SensorDataMapper mapper;
    private final AggregationStrategyFactory strategyFactory;

    /**
     * Endpoint for ingesting sensor data into the system.
     * Accepts a SensorRequest object, maps it to a SensorData entity, and saves it to the database.
     *
     * @param request the SensorRequest object containing sensor data to be ingested.
     * @return a ResponseEntity containing a success message if the data is saved successfully.
     */
    @Operation(summary = "Ingest sensor data", description = "Accepts sensor data and saves it to the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Data saved successfully",
                    content = @Content(schema = @Schema(implementation = SensorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = SensorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = SensorResponse.class)))
    })
    @PostMapping("/data")
    public ResponseEntity<Void> ingestSensorData(@Valid @RequestBody SensorRequest request) {
        log.info("Entered into method ::ingestSensorData");
        log.info("Ingesting data for device: {}, metric: {}", request.getDeviceId(), request.getMetric());

        SensorData entity = mapper.toEntity(request);
        log.info("Mapped entity: {}", entity);

        service.saveSensorData(entity);

        log.info("Exit from method ::ingestSensorData");

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    /**
     * Endpoint for retrieving aggregated statistics for a specific device and metric.
     * Fetches sensor data from the database and calculates aggregation results using the appropriate strategy.
     *
     * @param deviceId the identifier of the device.
     * @param metric the metric type (e.g., temperature, humidity).
     * @return a ResponseEntity containing the aggregation result or a no-content response if no data is found.
     */
    @Operation(summary = "Get aggregated statistics", description = "Fetches aggregated statistics for a specific device and metric.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aggregated statistics retrieved successfully",
                    content = @Content(schema = @Schema(implementation = SensorResponse.class))),
            @ApiResponse(responseCode = "204", description = "No content available for the specified device and metric",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters",
                    content = @Content(schema = @Schema(implementation = SensorResponse.class)))
    })
    @GetMapping("/data/stats")
    public ResponseEntity<SensorResponse<MetricSummary>> getSensorDataStats(
            @RequestParam @NotBlank(message = "deviceId cannot be blank") String deviceId,
            @RequestParam @NotBlank(message = "metric cannot be blank") String metric) {

        log.info("Entered into method ::getSensorDataStats");
        log.info("Fetching stats for device [{}] and metric [{}]", deviceId, metric);

        List<SensorData> dataList = service.getSensorData(deviceId, metric);

        if (dataList.isEmpty()) {
            log.warn("No data found for device [{}] and metric [{}]", deviceId, metric);
            return ResponseEntity.noContent().build();  // 204 No Content
        }

        MetricSummary result = strategyFactory.getStrategy(metric).aggregateMetrics(metric, dataList);

        log.info("Exit from method ::getSensorDataStats");

        return ResponseEntity.ok(
                SensorResponse.<MetricSummary>builder()
                        .success(true)
                        .message("Data retrieved successfully")
                        .data(result)
                        .build()
        );
    }

    /**
     * Endpoint for retrieving aggregated statistics for a specific device and metric within a time range.
     * Filters sensor data by the specified time range and calculates aggregation results using the appropriate strategy.
     *
     * @param deviceId the identifier of the device.
     * @param metric the metric type (e.g., temperature, humidity).
     * @param from the start of the time range (inclusive).
     * @param to the end of the time range (inclusive).
     * @return a ResponseEntity containing the aggregation result or a success response with no data if the range is empty.
     */
    @Operation(summary = "Get aggregated statistics by time range", description = "Fetches aggregated statistics for a specific device and metric within a specified time range.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aggregated statistics retrieved successfully or no data found",
                    content = @Content(schema = @Schema(implementation = SensorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters",
                    content = @Content(schema = @Schema(implementation = SensorResponse.class)))
    })
    @GetMapping("/data/stats/range")
    public ResponseEntity<SensorResponse<MetricSummary>> getSensorDataStatsByTimeRange(
            @RequestParam @NotBlank(message = "deviceId cannot be blank") String deviceId,
            @RequestParam @NotBlank(message = "metric cannot be blank") String metric,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {

        log.info("Entered into method ::getSensorDataStatsByTimeRange");
        log.info("Fetching stats for device [{}] and metric [{}]", deviceId, metric);

        List<SensorData> dataList = service.getSensorData(deviceId, metric);
        List<SensorData> filtered = TimeFilter.filterByRange(dataList, from, to);

        if (filtered.isEmpty()) {
            return ResponseEntity.ok(
                    SensorResponse.<MetricSummary>builder()
                            .success(false)
                            .message("No data in specified range")
                            .data(null)
                            .build()
            );
        }

        MetricSummary result = strategyFactory.getStrategy(metric).aggregateMetrics(metric, filtered);

        log.info("Exit from method ::getSensorDataStatsByTimeRange");

        return ResponseEntity.ok(
                SensorResponse.<MetricSummary>builder()
                        .success(true)
                        .message("Data in specified range retrieved successfully")
                        .data(result)
                        .build()
        );
    }

}

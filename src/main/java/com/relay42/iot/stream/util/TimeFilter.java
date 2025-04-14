package com.relay42.iot.stream.util;

import com.relay42.iot.stream.entity.SensorData;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for filtering sensor data based on a specified time range.
 * Provides a static method to filter a list of SensorData entities by their timestamp.
 * This is used to narrow down sensor data to a specific time window for analysis or aggregation.
 */
public class TimeFilter {

    /**
     * Filters a list of SensorData entities to include only those within the specified time range.
     * Compares the dataTimestamp of each SensorData entity with the provided 'from' and 'to' timestamps.
     *
     * @param dataList the list of SensorData entities to filter.
     * @param from the start of the time range (inclusive).
     * @param to the end of the time range (inclusive).
     * @return a list of SensorData entities that fall within the specified time range.
     */
    public static List<SensorData> filterByRange(List<SensorData> dataList, LocalDateTime from, LocalDateTime to) {
        return dataList.stream()
                .filter(d -> !d.getDataTimestamp().isBefore(from) && !d.getDataTimestamp().isAfter(to))
                .collect(Collectors.toList());
    }
}

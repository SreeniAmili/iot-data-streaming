package com.relay42.iot.stream.util;

import com.relay42.iot.stream.entity.SensorData;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for calculating the median value from a list of sensor data.
 * Provides a static method to compute the median of metric values from a list of SensorData entities.
 * This is used in aggregation operations to determine the central tendency of sensor data.
 */
public class MedianCalculator {

    /**
     * Calculates the median value of metric values from a list of SensorData entities.
     * Sorts the metric values and computes the median based on the size of the list.
     * Returns 0 if the list is empty.
     *
     * @param list the list of SensorData entities to calculate the median from.
     * @return the median value of the metric values, or 0 if the list is empty.
     */
    public static double calculate(List<SensorData> list) {
        List<Double> sorted = list.stream()
                .map(SensorData::getMetricValue)
                .sorted()
                .collect(Collectors.toList());

        int totalValues = sorted.size();
        if (totalValues == 0) return 0;

        return (totalValues % 2 == 1)
                ? sorted.get(totalValues / 2)
                : (sorted.get(totalValues / 2 - 1) + sorted.get(totalValues / 2)) / 2.0;
    }
}

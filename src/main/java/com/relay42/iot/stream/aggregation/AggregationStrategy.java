package com.relay42.iot.stream.aggregation;

import com.relay42.iot.stream.entity.SensorData;
import com.relay42.iot.stream.model.MetricSummary;


import java.util.List;

/**
 * Interface for defining aggregation strategies for sensor data.
 * Provides methods to calculate aggregation results for a specific metric
 * and to retrieve the list of supported metrics for the strategy.
 * This allows for flexible and extensible aggregation logic.
 */
public interface AggregationStrategy {

    /**
     * Calculates the aggregation result for a specific metric using the provided sensor data.
     * The implementation of this method depends on the specific aggregation strategy.
     *
     * @param metric the name of the metric to aggregate (e.g., temperature, humidity).
     * @param data the list of SensorData entities to be aggregated.
     * @return an AggregationResult containing the calculated values (e.g., min, max, avg, median).
     */
    MetricSummary aggregateMetrics(String metric, List<SensorData> data);

    /**
     * Retrieves the list of metrics supported by this aggregation strategy.
     * This allows the system to determine which metrics can be aggregated using this strategy.
     *
     * @return a list of supported metric names.
     */
    List<String> getSupportedMetrics() ;
}

package com.relay42.iot.stream.aggregation;
import com.relay42.iot.stream.aggregation.AggregationStrategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component

/**
 * Factory class for managing and providing aggregation strategies for sensor data.
 * Maintains a mapping of metrics to their corresponding aggregation strategies.
 * This allows for dynamic selection of the appropriate strategy based on the metric.
 */
public class AggregationStrategyFactory {

    private final Map<String, AggregationStrategy> strategyMap = new HashMap<>();

    /**
     * Constructs an AggregationStrategyFactory and initializes the strategy map.
     * Populates the map with supported metrics and their corresponding strategies.
     *
     * @param strategies the list of available AggregationStrategy implementations.
     */
    @Autowired
    public AggregationStrategyFactory(List<AggregationStrategy> strategies) {
        for (AggregationStrategy strategy : strategies) {
            for (String metric : strategy.getSupportedMetrics()) {
                strategyMap.put(metric.toLowerCase(), strategy);
            }
        }
    }

    /**
     * Retrieves the aggregation strategy for a specific metric.
     * Looks up the strategy map using the metric name (case-insensitive).
     *
     * @param metric the name of the metric for which the strategy is required.
     * @return the AggregationStrategy corresponding to the metric, or null if not found.
     */
    public AggregationStrategy getStrategy(String metric) {

        return strategyMap.get(metric.toLowerCase());
    }
}

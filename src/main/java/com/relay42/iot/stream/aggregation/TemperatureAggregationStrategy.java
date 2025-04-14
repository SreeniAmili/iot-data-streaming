package com.relay42.iot.stream.aggregation;

import com.relay42.iot.stream.entity.SensorData;
import com.relay42.iot.stream.model.MetricSummary;
import com.relay42.iot.stream.util.MedianCalculator;

import org.springframework.stereotype.Component;

import java.util.DoubleSummaryStatistics;
import java.util.List;

/**
 * Aggregation strategy implementation for calculating statistical metrics for temperature data.
 * Provides methods to calculate minimum, maximum, average, and median values for temperature-related metrics.
 * Implements the AggregationStrategy interface.
 */
@Component("temperature")
public class TemperatureAggregationStrategy implements AggregationStrategy {

    /**
     * Calculates the aggregation result for temperature-related metrics using the provided sensor data.
     * Computes statistical metrics such as minimum, maximum, average, and median values.
     * Returns default values (0.0) if the data list is null or empty.
     *
     * @param metric the name of the metric to aggregate (e.g., temperature, humidity).
     * @param dataList the list of SensorData entities to be aggregated.
     * @return an AggregationResult containing the calculated values (e.g., min, max, avg, median).
     */
    @Override
    public MetricSummary aggregateMetrics(String metric, List<SensorData> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            return MetricSummary.builder()
                    .metric(metric)
                    .min(0.0)
                    .max(0.0)
                    .avg(0.0)
                    .median(0.0)
                    .build();
        }

        DoubleSummaryStatistics stats = dataList.stream()
                .mapToDouble(SensorData::getMetricValue)
                .summaryStatistics();

        double median = MedianCalculator.calculate(dataList);

        return MetricSummary.builder()
                .metric(metric)
                .min(stats.getMin())
                .max(stats.getMax())
                .avg(stats.getAverage())
                .median(median)
                .build();
    }
    /**
     * Retrieves the list of metrics supported by this aggregation strategy.
     * This strategy supports temperature and humidity metrics.
     *
     * @return a list of supported metric names.
     */
    @Override
    public List<String> getSupportedMetrics() {
        return List.of("temperature", "humidity");
    }
}

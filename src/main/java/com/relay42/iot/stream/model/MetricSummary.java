package com.relay42.iot.stream.model;


import lombok.*;

/**
 * Model class representing the result of an aggregation operation on sensor data.
 * Contains statistical metrics such as minimum, maximum, average, and median values for a specific metric.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetricSummary {

    /**
     * The name of the metric (e.g., temperature, humidity) for which the aggregation is performed.
     */
    private String metric;

    /**
     * The minimum value of the metric in the aggregation result.
     */
    private Double min;

    /**
     * The maximum value of the metric in the aggregation result.
     */
    private Double max;

    /**
     * The average value of the metric in the aggregation result.
     */
    private Double avg;

    /**
     * The median value of the metric in the aggregation result.
     */
    private Double median;
}
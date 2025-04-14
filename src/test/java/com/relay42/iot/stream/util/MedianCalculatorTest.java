package com.relay42.iot.stream.util;

import com.relay42.iot.stream.entity.SensorData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MedianCalculatorTest {

    @Test
    void testCalculateMedianOddSize() {

        List<SensorData> sensorDataList = List.of(
                SensorData.builder().metricValue(10.0).build(),
                SensorData.builder().metricValue(20.0).build(),
                SensorData.builder().metricValue(30.0).build()
        );


        double median = MedianCalculator.calculate(sensorDataList);


        assertEquals(20.0, median, "Median should be the middle value for odd-sized list");
    }

    @Test
    void testCalculateMedianEvenSize() {

        List<SensorData> sensorDataList = List.of(
                SensorData.builder().metricValue(10.0).build(),
                SensorData.builder().metricValue(20.0).build(),
                SensorData.builder().metricValue(30.0).build(),
                SensorData.builder().metricValue(40.0).build()
        );


        double median = MedianCalculator.calculate(sensorDataList);


        assertEquals(25.0, median, "Median should be the average of two middle values for even-sized list");
    }

    @Test
    void testCalculateMedianEmptyList() {

        List<SensorData> sensorDataList = List.of();


        double median = MedianCalculator.calculate(sensorDataList);


        assertEquals(0.0, median, "Median should be 0 for an empty list");
    }

    @Test
    void testCalculateMedianSingleElement() {

        List<SensorData> sensorDataList = List.of(
                SensorData.builder().metricValue(15.0).build()
        );


        double median = MedianCalculator.calculate(sensorDataList);


        assertEquals(15.0, median, "Median should be the single element value for a list with one element");
    }

    @Test
    void testCalculateMedianUnsortedList() {

        List<SensorData> sensorDataList = List.of(
                SensorData.builder().metricValue(30.0).build(),
                SensorData.builder().metricValue(10.0).build(),
                SensorData.builder().metricValue(20.0).build()
        );


        double median = MedianCalculator.calculate(sensorDataList);


        assertEquals(20.0, median, "Median calculation should work for unsorted lists");
    }
}
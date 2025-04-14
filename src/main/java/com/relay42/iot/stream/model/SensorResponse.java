package com.relay42.iot.stream.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensorResponse<T> {
    private boolean success;
    private String message;
    private T data;
}
package com.relay42.iot.stream.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SensorRequest {
    @NotBlank
    private String deviceId;

    @NotBlank
    private String metric;

    @NotNull
    private Double metricValue;

    @NotNull
    private LocalDateTime dataTimestamp;

    private String deviceType;

}

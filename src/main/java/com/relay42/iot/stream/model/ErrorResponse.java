package com.relay42.iot.stream.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class ErrorResponse {
    private boolean success;
    private String message;
    private int status;
    private LocalDateTime timestamp;
    private Map<String, String> errors;
}


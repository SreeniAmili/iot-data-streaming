package com.relay42.iot.stream.exception;

/**
 * Custom exception class for handling business logic errors in the IoT Data Streaming API.
 * This exception is used to indicate issues that occur during the execution of business rules or validations.
 * It provides constructors for specifying error messages and optional root causes.
 */
public class BusinessException extends RuntimeException {

    /**
     * Constructs a new BusinessException with the specified error message.
     *
     * @param message the detail message explaining the reason for the exception.
     */
    public BusinessException(String message) {

        super(message);
    }

    /**
     * Constructs a new BusinessException with the specified error message and root cause.
     *
     * @param message the detail message explaining the reason for the exception.
     * @param cause the root cause of the exception (can be null).
     */
    public BusinessException(String message, Throwable cause) {

        super(message, cause);
    }
}
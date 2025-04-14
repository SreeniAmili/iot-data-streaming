-- Table to store sensor data
CREATE TABLE IF NOT EXISTS sensor_data (
    -- Unique identifier for each record
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    
    -- Identifier for the device sending the data
    device_id VARCHAR(255) NOT NULL,
    
    -- Type of metric being recorded (e.g., temperature, humidity)
    metric VARCHAR(255) NOT NULL,
    
    -- Value of the recorded metric
    metric_value DOUBLE NOT NULL,

    -- Type of the device (optional field)
    device_type VARCHAR(255),

     -- Timestamp of when the data was recorded
        data_timestamp TIMESTAMP NOT NULL
);
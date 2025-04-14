package com.relay42.iot.stream.mapper;

import com.relay42.iot.stream.entity.SensorData;

import com.relay42.iot.stream.model.SensorRequest;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between SensorRequest model and SensorData entity.
 * Utilizes MapStruct to generate the implementation at compile time.
 * This is used to map incoming API requests to the database entity.
 */
@Mapper(componentModel = "spring")
public interface SensorDataMapper {

    /**
     * Maps a SensorRequest object to a SensorData entity.
     *
     * @param request the SensorRequest object containing API request data.
     * @return a SensorData entity populated with the data from the request.
     */
    SensorData toEntity(SensorRequest request);
}

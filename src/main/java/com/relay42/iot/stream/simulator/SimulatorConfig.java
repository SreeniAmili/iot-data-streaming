package com.relay42.iot.stream.simulator;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "simulator")
@Data
public class SimulatorConfig {

    private boolean enabled;
    private long fixedRateMs;
    private int maxRecords;
    private boolean allowSpikes;


}

package com.airline.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "cache.flights")
public class CacheProperties {

    private int ttlMinutes;
    private int maxSize;

}

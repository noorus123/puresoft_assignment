package com.airline.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class CacheConfig {

    private final CacheProperties cacheProperties;

    @Bean
    public CacheManager cacheManager() {

        CaffeineCacheManager manager =
                new CaffeineCacheManager("flights");

        manager.setCaffeine(
                Caffeine.newBuilder()
                        .expireAfterWrite(
                                cacheProperties.getTtlMinutes(),
                                TimeUnit.MINUTES
                        )
                        .maximumSize(
                                cacheProperties.getMaxSize()
                        )
        );

        return manager;
    }
}

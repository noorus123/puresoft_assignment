package com.airline.flight.service;

import com.airline.flight.domain.Flight;
import com.airline.flight.repository.FlightRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.annotation.EnableCaching;

import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
@EnableCaching
class FlightServiceCacheTest {

    @Autowired
    private FlightService flightService;

    @MockBean
    private FlightRepository flightRepository;

    @Test
    void shouldCacheFlights() {

        List<Flight> flights = List.of(
                Flight.builder()
                        .id(1L)
                        .flightNumber("AI-101")
                        .source("Delhi")
                        .destination("Mumbai")
                        .build()
        );

        when(flightRepository.findAll()).thenReturn(flights);

        // First call - hits DB
        flightService.getAllFlights();

        // Second call - should hit cache
        flightService.getAllFlights();

        verify(flightRepository, times(1)).findAll();
    }
}

package com.airline.flight.service;

import com.airline.flight.domain.Flight;
import com.airline.flight.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;

    @Cacheable("flights")
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }
}

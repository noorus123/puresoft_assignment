package com.airline.flight.controller;

import com.airline.flight.domain.Flight;
import com.airline.flight.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @GetMapping
    public List<Flight> getFlights() {
        return flightService.getAllFlights();
    }
}

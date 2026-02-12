package com.airline.inventory.controller;

import com.airline.inventory.domain.Seat;
import com.airline.inventory.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatRepository seatRepository;

    @GetMapping("/{flightId}")
    public List<Seat> getSeats(@PathVariable Long flightId) {
        return seatRepository.findAll()
                .stream()
                .filter(seat -> seat.getFlightId().equals(flightId))
                .toList();
    }
}

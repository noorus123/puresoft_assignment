package com.airline.booking.controller;

import com.airline.booking.domain.Booking;
import com.airline.booking.dto.BookingRequest;
import com.airline.booking.dto.BookingResponse;
import com.airline.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingResponse createBooking(@RequestBody BookingRequest request) {

        Booking booking = bookingService.createBooking(
                request.getFlightId(),
                request.getSeatNumber(),
                request.getPassengerName()
        );

        return BookingResponse.builder()
                .bookingId(booking.getId())
                .flightId(booking.getFlightId())
                .seatNumber(booking.getSeatNumber())
                .passengerName(booking.getPassengerName())
                .status(booking.getStatus())
                .build();
    }

    @GetMapping("/{id}")
    public BookingResponse getBooking(@PathVariable Long id) {

        Booking booking = bookingService.getBooking(id);

        return BookingResponse.builder()
                .bookingId(booking.getId())
                .flightId(booking.getFlightId())
                .seatNumber(booking.getSeatNumber())
                .passengerName(booking.getPassengerName())
                .status(booking.getStatus())
                .build();
    }

}

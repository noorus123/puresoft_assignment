package com.airline.booking.dto;

import lombok.Data;

@Data
public class BookingRequest {

    private Long flightId;
    private String seatNumber;
    private String passengerName;
}

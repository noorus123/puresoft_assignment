package com.airline.booking.dto;

import com.airline.booking.domain.BookingStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingResponse {

    private Long bookingId;
    private Long flightId;
    private String seatNumber;
    private String passengerName;
    private BookingStatus status;
}

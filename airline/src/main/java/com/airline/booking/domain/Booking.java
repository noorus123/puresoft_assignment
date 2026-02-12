package com.airline.booking.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long flightId;

    private String seatNumber;

    private String passengerName;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}

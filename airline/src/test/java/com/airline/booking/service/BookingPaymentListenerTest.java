package com.airline.booking.service;

import com.airline.booking.domain.*;
import com.airline.booking.repository.BookingRepository;
import com.airline.inventory.domain.*;
import com.airline.inventory.repository.SeatRepository;
import com.airline.payment.event.PaymentSuccessEvent;
import com.airline.payment.event.PaymentFailedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.mockito.Mockito.*;

class BookingPaymentListenerTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private BookingPaymentListener listener;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldConfirmBookingOnPaymentSuccess() {

        Booking booking = Booking.builder()
                .id(1L)
                .flightId(1L)
                .seatNumber("A1")
                .status(BookingStatus.PENDING_PAYMENT)
                .build();

        Seat seat = Seat.builder()
                .flightId(1L)
                .seatNumber("A1")
                .status(SeatStatus.LOCKED)
                .build();

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(seatRepository.findByFlightIdAndSeatNumber(1L, "A1"))
                .thenReturn(Optional.of(seat));

        listener.handlePaymentSuccess(new PaymentSuccessEvent(1L));

        assert booking.getStatus() == BookingStatus.CONFIRMED;
        assert seat.getStatus() == SeatStatus.BOOKED;
    }

    @Test
    void shouldReleaseSeatOnPaymentFailure() {

        Booking booking = Booking.builder()
                .id(1L)
                .flightId(1L)
                .seatNumber("A1")
                .status(BookingStatus.PENDING_PAYMENT)
                .build();

        Seat seat = Seat.builder()
                .flightId(1L)
                .seatNumber("A1")
                .status(SeatStatus.LOCKED)
                .build();

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(seatRepository.findByFlightIdAndSeatNumber(1L, "A1"))
                .thenReturn(Optional.of(seat));

        listener.handlePaymentFailure(new PaymentFailedEvent(1L));

        assert booking.getStatus() == BookingStatus.FAILED;
        assert seat.getStatus() == SeatStatus.AVAILABLE;
    }
}

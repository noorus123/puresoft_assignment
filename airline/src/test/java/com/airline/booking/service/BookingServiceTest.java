package com.airline.booking.service;

import com.airline.booking.domain.*;
import com.airline.booking.repository.BookingRepository;
import com.airline.inventory.domain.*;
import com.airline.inventory.repository.SeatRepository;
import com.airline.payment.event.PaymentInitiatedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ApplicationEventPublisher publisher;

    @InjectMocks
    private BookingService bookingService;

    @Test
    void shouldCreateBookingSuccessfully() {

        Seat seat = Seat.builder()
                .flightId(1L)
                .seatNumber("A1")
                .status(SeatStatus.AVAILABLE)
                .build();

        when(seatRepository.findByFlightIdAndSeatNumber(1L, "A1"))
                .thenReturn(Optional.of(seat));

        when(bookingRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Booking result = bookingService.createBooking(1L, "A1", "Noor");

        assertEquals(BookingStatus.PENDING_PAYMENT, result.getStatus());
        verify(publisher).publishEvent(any(PaymentInitiatedEvent.class));
    }

    @Test
    void shouldThrowIfSeatNotAvailable() {

        Seat seat = Seat.builder()
                .flightId(1L)
                .seatNumber("A1")
                .status(SeatStatus.BOOKED)
                .build();

        when(seatRepository.findByFlightIdAndSeatNumber(1L, "A1"))
                .thenReturn(Optional.of(seat));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> bookingService.createBooking(1L, "A1", "Noor"));

        assertEquals("Seat not available", ex.getMessage());
    }

    @Test
    void shouldThrowIfSeatNotFound() {

        when(seatRepository.findByFlightIdAndSeatNumber(1L, "A1"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> bookingService.createBooking(1L, "A1", "Noor"));
    }
}

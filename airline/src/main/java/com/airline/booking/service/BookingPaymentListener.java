package com.airline.booking.service;

import com.airline.booking.domain.*;
import com.airline.booking.repository.BookingRepository;
import com.airline.inventory.domain.*;
import com.airline.inventory.repository.SeatRepository;
import com.airline.payment.event.PaymentSuccessEvent;
import com.airline.payment.event.PaymentFailedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookingPaymentListener {

    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;

    @Transactional
    @EventListener
    public void handlePaymentSuccess(PaymentSuccessEvent event) {

        Booking booking = bookingRepository.findById(event.bookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setStatus(BookingStatus.CONFIRMED);

        Seat seat = seatRepository.findByFlightIdAndSeatNumber(
                booking.getFlightId(),
                booking.getSeatNumber()
        ).orElseThrow(() -> new RuntimeException("Seat not found"));

        seat.setStatus(SeatStatus.BOOKED);
    }

    @Transactional
    @EventListener
    public void handlePaymentFailure(PaymentFailedEvent event) {

        Booking booking = bookingRepository
                .findById(event.bookingId())
                .orElseThrow();

        booking.setStatus(BookingStatus.FAILED);

        Seat seat = seatRepository
                .findByFlightIdAndSeatNumber(
                        booking.getFlightId(),
                        booking.getSeatNumber()
                )
                .orElseThrow();

        seat.setStatus(SeatStatus.AVAILABLE);
        seat.setLockExpiryTime(null);

        seatRepository.save(seat);
    }
}

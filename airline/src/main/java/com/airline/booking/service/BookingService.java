package com.airline.booking.service;

import com.airline.booking.domain.*;
import com.airline.booking.repository.BookingRepository;
import com.airline.inventory.domain.*;
import com.airline.inventory.repository.SeatRepository;
import com.airline.payment.event.PaymentInitiatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.orm.ObjectOptimisticLockingFailureException;


@Service
@RequiredArgsConstructor
public class BookingService {

    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Booking createBooking(Long flightId, String seatNumber, String passengerName) {
        try {
            Seat seat = seatRepository.findByFlightIdAndSeatNumber(flightId, seatNumber)
                    .orElseThrow(() -> new RuntimeException("Seat not found"));

            if (seat.getStatus() != SeatStatus.AVAILABLE) {
                throw new RuntimeException("Seat not available");
            }

            seat.setStatus(SeatStatus.LOCKED);
            seatRepository.save(seat);

            Booking booking = Booking.builder()
                    .flightId(flightId)
                    .seatNumber(seatNumber)
                    .passengerName(passengerName)
                    .status(BookingStatus.PENDING_PAYMENT)
                    .build();

            bookingRepository.save(booking);

            eventPublisher.publishEvent(new PaymentInitiatedEvent(booking.getId()));

            return booking;

        } catch (ObjectOptimisticLockingFailureException ex) {
            throw new RuntimeException("Seat already booked by another user");
        }
    }


    public Booking getBooking(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

}

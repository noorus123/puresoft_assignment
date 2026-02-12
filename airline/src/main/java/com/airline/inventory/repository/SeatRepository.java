package com.airline.inventory.repository;

import com.airline.inventory.domain.Seat;
import com.airline.inventory.domain.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    Optional<Seat> findByFlightIdAndSeatNumber(Long flightId, String seatNumber);

    List<Seat> findByStatusAndLockExpiryTimeBefore(SeatStatus status, LocalDateTime time);

    List<Seat> findByFlightId(Long flightId);


}

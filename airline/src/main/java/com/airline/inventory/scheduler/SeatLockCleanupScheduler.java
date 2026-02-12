package com.airline.inventory.scheduler;

import com.airline.inventory.domain.Seat;
import com.airline.inventory.domain.SeatStatus;
import com.airline.inventory.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SeatLockCleanupScheduler {

    private final SeatRepository seatRepository;

    @Scheduled(fixedRate = 60000)
    public void releaseExpiredSeats() {

        List<Seat> expiredSeats =
                seatRepository.findByStatusAndLockExpiryTimeBefore(
                        SeatStatus.LOCKED,
                        LocalDateTime.now()
                );

        for (Seat seat : expiredSeats) {
            seat.setStatus(SeatStatus.AVAILABLE);
            seat.setLockExpiryTime(null);
        }

        if (!expiredSeats.isEmpty()) {
            seatRepository.saveAll(expiredSeats);
            System.out.println("Released expired seat locks: "
                    + expiredSeats.size());
        }
    }
}

package com.airline.config;

import com.airline.flight.domain.Flight;
import com.airline.flight.repository.FlightRepository;
import com.airline.inventory.domain.Seat;
import com.airline.inventory.domain.SeatStatus;
import com.airline.inventory.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;



@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final SeatRepository seatRepository;
    private final FlightRepository flightRepository;

    @Override
    public void run(String... args) {

        if (seatRepository.count() == 0) {

            for (int i = 1; i <= 5; i++) {
                seatRepository.save(
                        Seat.builder()
                                .flightId(1L)
                                .seatNumber("A" + i)
                                .status(SeatStatus.AVAILABLE)
                                .build()
                );
            }

            System.out.println("Sample seats created for flightId = 1");
        }

        if (flightRepository.count() == 0) {

            Flight f1 = Flight.builder()
                    .flightNumber("AI-101")
                    .source("Delhi")
                    .destination("Mumbai")
                    .build();

            Flight f2 = Flight.builder()
                    .flightNumber("AI-202")
                    .source("Bangalore")
                    .destination("Chennai")
                    .build();

            flightRepository.save(f1);
            flightRepository.save(f2);

            System.out.println("Sample flights created");
        }

    }
}

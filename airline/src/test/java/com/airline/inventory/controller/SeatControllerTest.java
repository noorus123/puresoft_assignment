package com.airline.inventory.controller;

import com.airline.booking.domain.*;
import com.airline.inventory.domain.Seat;
import com.airline.inventory.domain.SeatStatus;
import com.airline.inventory.repository.SeatRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(SeatController.class)
class SeatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SeatRepository seatRepository;


    @Test
    void shouldReturnSeats() throws Exception {

        Seat seat = Seat.builder()
                .id(1L)
                .flightId(1L)
                .seatNumber("A1")
                .status(SeatStatus.AVAILABLE)
                .build();

        when(seatRepository.findByFlightId(1L))
                .thenReturn(List.of(seat));

        mockMvc.perform(get("/api/seats/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].seatNumber")
                        .value("A1"));
    }

}

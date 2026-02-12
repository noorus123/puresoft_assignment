package com.airline.booking.controller;

import com.airline.booking.domain.*;
import com.airline.booking.service.BookingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Test
    void shouldCreateBooking() throws Exception {

        Booking booking = Booking.builder()
                .id(1L)
                .flightId(1L)
                .seatNumber("A1")
                .passengerName("Noor")
                .status(BookingStatus.PENDING_PAYMENT)
                .build();

        Mockito.when(bookingService.createBooking(1L, "A1", "Noor"))
                .thenReturn(booking);

        String request = """
                {
                    "flightId": 1,
                    "seatNumber": "A1",
                    "passengerName": "Noor"
                }
                """;

        mockMvc.perform(post("/api/bookings")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingId").value(1));
    }

    @Test
    void shouldGetBooking() throws Exception {

        Booking booking = Booking.builder()
                .id(1L)
                .flightId(1L)
                .seatNumber("A1")
                .passengerName("Noor")
                .status(BookingStatus.CONFIRMED)
                .build();

        Mockito.when(bookingService.getBooking(1L))
                .thenReturn(booking);

        mockMvc.perform(get("/api/bookings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void shouldReturnBadRequestWhenSeatUnavailable() throws Exception {

        Mockito.when(bookingService.createBooking(1L, "A1", "Noor"))
                .thenThrow(new RuntimeException("Seat not available"));

        String request = """
            {
                "flightId": 1,
                "seatNumber": "A1",
                "passengerName": "Noor"
            }
            """;

        mockMvc.perform(post("/api/bookings")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Seat not available"));
    }


}


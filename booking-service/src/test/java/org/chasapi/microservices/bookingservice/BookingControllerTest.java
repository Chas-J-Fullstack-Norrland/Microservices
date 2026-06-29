package org.chasapi.microservices.bookingservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.chasapi.microservices.bookingservice.controller.BookingController;
import org.chasapi.microservices.bookingservice.model.Booking;
import org.chasapi.microservices.bookingservice.security.SecurityConfig;
import org.chasapi.microservices.bookingservice.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = BookingController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class}
)
@Import(SecurityConfig.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookingService bookingService;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Autowired
    private ObjectMapper objectMapper;
    private Booking booking;

    @BeforeEach
    void setUp() {
        booking = new Booking(1L, 10L, 3, LocalDate.of(2026, 7, 1), "PENDING");
    }

    @Test
    void getAll_returnsOkWithList() throws Exception {
        when(bookingService.getAllBookings()).thenReturn(List.of(booking));

        mockMvc.perform(get("/api/v1/bookings").with(SecurityMockMvcRequestPostProcessors.jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    @Test
    void getById_found_returnsOk() throws Exception {
        when(bookingService.getBookingById(1L)).thenReturn(Optional.of(booking));

        mockMvc.perform(get("/api/v1/bookings/1").with(SecurityMockMvcRequestPostProcessors.jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getById_notFound_returns404() throws Exception {
        when(bookingService.getBookingById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/bookings/99").with(SecurityMockMvcRequestPostProcessors.jwt()))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_returnsCreated() throws Exception {
        when(bookingService.createBooking(any(Booking.class))).thenReturn(booking);

        mockMvc.perform(post("/api/v1/bookings")
                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void updateStatus_returnsOkWithUpdatedBooking() throws Exception {
        Booking updated = new Booking(1L, 10L, 3, LocalDate.of(2026, 7, 1), "CONFIRMED");
        when(bookingService.updateStatus(1L, "CONFIRMED")).thenReturn(updated);

        mockMvc.perform(put("/api/v1/bookings/1/status")
                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                        .param("status", "CONFIRMED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void delete_returnsNoContent() throws Exception {
        doNothing().when(bookingService).deleteBooking(1L);

        mockMvc.perform(delete("/api/v1/bookings/1").with(SecurityMockMvcRequestPostProcessors.jwt()))
                .andExpect(status().isNoContent());
    }
}
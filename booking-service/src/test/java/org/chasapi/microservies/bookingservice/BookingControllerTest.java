package org.chasapi.microservies.bookingservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.chasapi.microservies.bookingservice.Controller.BookingController;
import org.chasapi.microservies.bookingservice.model.Booking;
import org.chasapi.microservies.bookingservice.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookingService bookingService;

    private ObjectMapper objectMapper;
    private Booking booking;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        booking = new Booking(1L, 10L, 3, LocalDate.of(2026, 7, 1), "PENDING");
    }

    @Test
    void getAll_returnsOkWithList() throws Exception {
        when(bookingService.getAllBookings()).thenReturn(List.of(booking));

        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    @Test
    void getByUser_returnsOkWithList() throws Exception {
        when(bookingService.getBookingsByUser(10L)).thenReturn(List.of(booking));

        mockMvc.perform(get("/api/bookings/user/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(10));
    }

    @Test
    void getById_found_returnsOk() throws Exception {
        when(bookingService.getBookingById(1L)).thenReturn(Optional.of(booking));

        mockMvc.perform(get("/api/bookings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getById_notFound_returns404() throws Exception {
        when(bookingService.getBookingById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/bookings/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_returnsCreated() throws Exception {
        when(bookingService.createBooking(any(Booking.class))).thenReturn(booking);

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void updateStatus_returnsOkWithUpdatedBooking() throws Exception {
        Booking updated = new Booking(1L, 10L, 3, LocalDate.of(2026, 7, 1), "CONFIRMED");
        when(bookingService.updateStatus(1L, "CONFIRMED")).thenReturn(updated);

        mockMvc.perform(put("/api/bookings/1/status")
                        .param("status", "CONFIRMED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void delete_returnsNoContent() throws Exception {
        doNothing().when(bookingService).deleteBooking(1L);

        mockMvc.perform(delete("/api/bookings/1"))
                .andExpect(status().isNoContent());
    }
}
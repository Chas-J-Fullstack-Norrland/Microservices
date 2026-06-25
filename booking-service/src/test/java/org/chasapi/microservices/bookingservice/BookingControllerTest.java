package org.chasapi.microservices.bookingservice;

import org.chasapi.microservices.bookingservice.model.Booking;
import org.chasapi.microservices.bookingservice.repository.BookingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class BookingControllerTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("testdb").withUsername("test").withPassword("test");
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        /*registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");*/
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookingRepository repository;
    @BeforeEach
    void setup() {
        repository.deleteAll();
    }
    @Test
    void shouldCreateBooking() throws Exception {
        mockMvc.perform(post("/api/bookings").contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                "date": "2026-01-01", "timeslot" : 2, "userId": 1
                }
                """)).andExpect(status().isCreated()).andExpect(jsonPath("$.userId").value(1));
    }
    @Test
    void shouldGetAllBookings() throws Exception {
       Booking booking = new Booking();
       booking.setDate(LocalDate.now());
       booking.setTimeslot(1);
       booking.setUserId(1L);
       repository.save(booking);
        mockMvc.perform(get("/api/bookings")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1));
    }
    @Test
    void shouldGetBookingById() throws Exception {
        Booking saved = repository.save(new Booking(null, LocalDate.now(), 1, 1L));
        mockMvc.perform(get("/api/bookings/" + saved.getId())).andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1));
    }
    @Test
    void shouldCreateThenGetById() throws Exception {
        String json = """
                { "date": "2026-01-01", "timeslot": 1, "userId": 1}
                """;
        String response = mockMvc.perform(post("/api/bookings").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        mockMvc.perform(get("/api/bookings")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1));
    }
    @Test
    void shouldDeleteBooking() throws Exception {
        Booking saved = repository.save(new Booking(null, LocalDate.now(),1, 1L));
        mockMvc.perform(delete("/api/bookings/" + saved.getId())).andExpect(status().isNoContent());
    }
}
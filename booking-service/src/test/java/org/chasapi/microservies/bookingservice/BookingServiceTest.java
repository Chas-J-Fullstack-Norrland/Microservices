package org.chasapi.microservies.bookingservice;

import org.chasapi.microservies.bookingservice.model.Booking;
import org.chasapi.microservies.bookingservice.service.BookingService;
import org.hibernate.dialect.aggregate.PostgreSQLAggregateSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.circuitbreaker.httpservice.HttpServiceFallback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Testcontainers
class BookingServiceTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");
    @DynamicPropertySource
    static void props(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
    @Autowired
    private BookingService service;
    @Test
    void createBooking_shouldCacheAndSave() {
        Booking booking = new Booking(new Date(), 1, 1L);
        Booking saved = service.createBooking(booking);
        assertNotNull(saved.getId());
        service.getAllBookings();
    }
}

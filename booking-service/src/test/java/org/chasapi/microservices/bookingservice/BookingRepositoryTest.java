package org.chasapi.microservices.bookingservice;

import org.chasapi.microservices.bookingservice.model.Booking;
import org.chasapi.microservices.bookingservice.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {
        "spring.cloud.config.enabled=false",
        "eureka.client.enabled=false",
        "spring.security.oauth2.resourceserver.jwt.secret-key=secret-key-here",
        "spring.jpa.hibernate.ddl-auto=update",
        "spring.jpa.show-sql=true"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@ActiveProfiles("test")
public class BookingRepositoryTest {

    @Container
    static PostgreSQLContainer<?> dbContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("booking-service-test")
            .withUsername("admin")
            .withPassword("password");

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", dbContainer::getJdbcUrl);
        registry.add("spring.datasource.username", dbContainer::getUsername);
        registry.add("spring.datasource.password", dbContainer::getPassword);
    }

    @Autowired
    private BookingRepository bookingRepository;

    @BeforeEach
    void cleanUp() {
        bookingRepository.deleteAll();
    }

    @Test
    void contextLoads() {
    }

    @Test
    void save_andFindById_works() {
        Booking booking = new Booking(null, 5L, 2, LocalDate.of(2026, 8, 1), "PENDING");
        Booking saved = bookingRepository.save(booking);

        assertThat(saved.getId()).isNotNull();
        assertThat(bookingRepository.findById(saved.getId())).contains(saved);
    }

    @Test
    void findByUserId_returnsMatchingBookings() {
        bookingRepository.save(new Booking(null, 5L, 1, LocalDate.of(2026, 8, 1), "PENDING"));
        bookingRepository.save(new Booking(null, 5L, 2, LocalDate.of(2026, 8, 2), "CONFIRMED"));
        bookingRepository.save(new Booking(null, 9L, 3, LocalDate.of(2026, 8, 3), "PENDING"));

        List<Booking> result = bookingRepository.findByUserId(5L);

        assertThat(result).hasSize(2)
                .allMatch(b -> b.getUserId().equals(5L));
    }

    @Test
    void findByUserId_noMatch_returnsEmpty() {
        List<Booking> result = bookingRepository.findByUserId(999L);

        assertThat(result).isEmpty();
    }

    @Test
    void findAll_returnsAllSavedBookings() {
        bookingRepository.save(new Booking(null, 1L, 1, LocalDate.of(2026, 8, 1), "PENDING"));
        bookingRepository.save(new Booking(null, 2L, 2, LocalDate.of(2026, 8, 2), "CONFIRMED"));

        List<Booking> result = bookingRepository.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void deleteById_removesBooking() {
        Booking saved = bookingRepository.save(
                new Booking(null, 5L, 1, LocalDate.of(2026, 8, 1), "PENDING"));

        bookingRepository.deleteById(saved.getId());

        assertThat(bookingRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    void save_updatesExistingBooking() {
        Booking saved = bookingRepository.save(
                new Booking(null, 5L, 1, LocalDate.of(2026, 8, 1), "PENDING"));

        saved.setStatus("CONFIRMED");
        Booking updated = bookingRepository.save(saved);

        assertThat(updated.getStatus()).isEqualTo("CONFIRMED");
        assertThat(bookingRepository.findAll()).hasSize(1);
    }
}

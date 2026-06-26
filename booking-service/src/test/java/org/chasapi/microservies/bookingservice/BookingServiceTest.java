package org.chasapi.microservies.bookingservice;



import org.chasapi.microservies.bookingservice.model.Booking;
import org.chasapi.microservies.bookingservice.repository.BookingRepository;
import org.chasapi.microservies.bookingservice.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingService bookingService;

    private Booking booking;

    @BeforeEach
    void setUp() {
        booking = new Booking(1L, 10L, 3, LocalDate.of(2026, 7, 1), "PENDING");
    }

    @Test
    void createBooking_setsPendingStatusAndSaves() {
        Booking input = new Booking(null, 10L, 3, LocalDate.of(2026, 7, 1), null);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking result = bookingService.createBooking(input);

        assertThat(input.getStatus()).isEqualTo("PENDING");
        assertThat(result).isEqualTo(booking);
        verify(bookingRepository).save(input);
    }

    @Test
    void getBookingsByUser_returnsListForUser() {
        when(bookingRepository.findByUserId(10L)).thenReturn(List.of(booking));

        List<Booking> result = bookingService.getBookingsByUser(10L);

        assertThat(result).containsExactly(booking);
        verify(bookingRepository).findByUserId(10L);
    }

    @Test
    void getBookingById_returnsOptional() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        Optional<Booking> result = bookingService.getBookingById(1L);

        assertThat(result).contains(booking);
    }

    @Test
    void getBookingById_notFound_returnsEmpty() {
        when(bookingRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Booking> result = bookingService.getBookingById(99L);

        assertThat(result).isEmpty();
    }

    @Test
    void getAllBookings_returnsList() {
        when(bookingRepository.findAll()).thenReturn(List.of(booking));

        List<Booking> result = bookingService.getAllBookings();

        assertThat(result).hasSize(1);
    }

    @Test
    void updateStatus_updatesAndSaves() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);

        Booking result = bookingService.updateStatus(1L, "CONFIRMED");

        assertThat(result.getStatus()).isEqualTo("CONFIRMED");
        verify(bookingRepository).save(booking);
    }

    @Test
    void updateStatus_notFound_throwsException() {
        when(bookingRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.updateStatus(99L, "CONFIRMED"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Booking not found");
    }

    @Test
    void deleteBooking_callsDeleteById() {
        doNothing().when(bookingRepository).deleteById(1L);

        bookingService.deleteBooking(1L);

        verify(bookingRepository).deleteById(1L);
    }
}


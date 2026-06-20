package org.chasapi.microservices.service;
import org.chasapi.microservices.model.Booking;
import org.chasapi.microservices.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {
    private final BookingRepository repository;
    public BookingService(BookingRepository repository) {
        this.repository = repository;
    }
    public List<Booking> getAllBookings() {
        return repository.findAll();
    }
    public Booing getBookingById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }
    public void deleteBooking(Long id) {
        repository.deleteById(id);
    }
}
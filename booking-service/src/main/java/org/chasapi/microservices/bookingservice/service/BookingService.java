package org.chasapi.microservices.bookingservice.service;
import org.chasapi.microservices.bookingservice.model.Booking;
import org.chasapi.microservices.bookingservice.repository.BookingRepository;
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

    public Booking getBookingById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    public Booking createBooking(Booking booking) {
        return repository.save(booking);
    }

    public Booking updateBooking(Long id, Booking updated) {
        Booking existing = getBookingById(id);

        existing.setDate(updated.getDate());
        existing.setTimeslot(updated.getTimeslot());
        existing.setUserId(updated.getUserId());

        return repository.save(existing);
    }

    public void deleteBooking(Long id) {
        repository.deleteById(id);
    }
}

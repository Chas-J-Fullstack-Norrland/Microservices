package org.chasapi.microservices.service;
import org.chasapi.microservices.model.Booking;
import org.chasapi.microservices.repository.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import java.util.List;

@Service
public class BookingService {
    private final BookingRepository repository;
    public BookingService(BookingRepository repository) {
        this.repository = repository;
    }
    @Cacheable("bookings")
    public List<Booking> getAllBookings() {
        return repository.findAll();
    }
    @Cacheable(value = "booking", key = "#id")
    public Booking getBookingById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }
    @CacheEvict(value = {"bookings", "booking"}, allEntries = true)
    public Booking createBooking(Booking booking) {
        return repository.save(booking);
    }
    @CacheEvict(value = {"bookings", "booking"}, allEntries = true)
    public void deleteBooking(Long id) {
        repository.deleteById(id);
    }
    @CacheEvict(value = "booking", key = "#id")
    public void deleteSingleCache(Long id) {
        repository.deleteById(id);
    }
}
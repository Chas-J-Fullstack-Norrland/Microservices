package org.chasapi.microservies.bookingservice.service;

import org.chasapi.microservies.bookingservice.model.Booking;
import org.chasapi.microservies.bookingservice.repository.BookingRepository;
/*import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;*/
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {
    private final BookingRepository repository;
    public BookingService(BookingRepository repository) {
        this.repository = repository;
    }
    /*@Cacheable("bookings")*/
    public List<Booking> getAllBookings() {
        return repository.findAll();
    }
    /*@Cacheable(value = "booking", key = "#id")*/
    public Booking getBookingById(Long id) {
        return repository.findById(id).orElseThrow();
    }
    /*@CacheEvict(value = {"bookings", "booking"}, allEntries = true)*/
    public Booking createBooking(Booking booking) {
        return repository.save(booking);
    }
    public Booking updateBooking(Long id, Booking booking) {
        Booking existing = getBookingById(id);
        existing.setDate(booking.getDate());
        existing.setTimeslot(booking.getTimeslot());
        existing.setUserId(booking.getUserId());
        return repository.save(existing);
        /*.setId(id);
        return repository.save(booking);*/
    }
    public void deleteBooking(Long id) {
        repository.deleteById(id);
    }
}

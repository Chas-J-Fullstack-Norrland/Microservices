package org.chasapi.microservies.bookingservice.controller;

import jakarta.ws.rs.Path;
import org.chasapi.microservies.bookingservice.model.Booking;
import org.chasapi.microservies.bookingservice.service.BookingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService service;
    public BookingController(BookingService service) {
        this.service = service;
    }
    @GetMapping
    public List<Booking> getAll() {
        return service.getAllBookings();
    }
    @GetMapping("/{id}")
    public Booking getById(@PathVariable Long id) {
        return service.getBookingById(id);
    }
    @PostMapping
    public Booking create(@RequestBody Booking booking) {
        return service.createBooking(booking);
    }
    @PutMapping("/{id}")
    public Booking update(
            @PathVariable Long id,
            @RequestBody Booking booking) {
        return service.updateBooking(id, booking);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteBooking(id);
    }

}

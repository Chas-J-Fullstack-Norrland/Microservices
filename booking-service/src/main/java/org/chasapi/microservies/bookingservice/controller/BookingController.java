package org.chasapi.microservies.bookingservice.controller;

import org.chasapi.microservies.bookingservice.model.Booking;
import org.chasapi.microservies.bookingservice.service.BookingService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService service;
    public BookingController(BookingService service) {
        this.service = service;
    }
    @GetMapping
    public List<Booking> getAllBookings(Authentication auth) {
        return service.getAllBookings();
    }
    @GetMapping("/{id}")
    public Booking getBooking(@PathVariable Long id) {
        return service.getBookingById(id);
    }
    @PostMapping
    public Booking createBooking(@RequestBody Booking booking) {
        return service.createBooking(booking);
    }
    @DeleteMapping("/{id}")
    public void deleteBooking(@PathVariable Long id) {
        service.deleteBooking(id);
    }

}

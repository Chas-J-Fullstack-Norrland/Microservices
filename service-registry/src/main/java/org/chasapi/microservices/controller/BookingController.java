package org.chasapi.microservices.controller;

import org.chasapi.microservices.model.Booking;
import org.chasapi.microservices.service.BookingService;
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
    public List<Booking> getBookings(HttpServletRequest request) {
        String user = (String) request.getAttribute("user");
        System.out.println("Request from user: " + user);
        return service.getAllBookings();
    }
    @GetMapping
    public List<Booking> getAllBookings() {
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
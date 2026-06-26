package org.chasapi.microservies.bookingservice.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.chasapi.microservies.bookingservice.dto.BookingRequest;
import org.chasapi.microservies.bookingservice.dto.BookingResponse;
import org.chasapi.microservies.bookingservice.model.Booking;
import org.chasapi.microservies.bookingservice.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public ResponseEntity<List<BookingResponse>> getAll() {
        List<Booking> bookings = bookingService.getAllBookings();
        List<BookingResponse> responses = bookings.stream()
                .map(BookingResponse::fromBookingResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingResponse>> getByUser(@PathVariable Long userId) {
        List<Booking> bookings = bookingService.getBookingsByUser(userId);
        List<BookingResponse> responses = bookings.stream()
                .map(BookingResponse::fromBookingResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getById(@PathVariable Long id) {
        return bookingService.getBookingById(id)
                .map(BookingResponse::fromBookingResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BookingResponse> create(@RequestBody @Valid BookingRequest request) {
        Booking toSave = request.toBookingDto();
        Booking saved = bookingService.createBooking(toSave);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BookingResponse.fromBookingResponse(saved));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<BookingResponse> updateStatus(@PathVariable Long id,
                                                        @RequestParam String status) {
        Booking updated = bookingService.updateStatus(id, status);
        return ResponseEntity.ok(BookingResponse.fromBookingResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
}

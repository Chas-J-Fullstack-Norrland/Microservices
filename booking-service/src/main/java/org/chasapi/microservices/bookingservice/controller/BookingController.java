package org.chasapi.microservices.bookingservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import org.chasapi.microservices.bookingservice.dto.BookingRequest;
import org.chasapi.microservices.bookingservice.dto.BookingResponse;
import org.chasapi.microservices.bookingservice.model.Booking;
import org.chasapi.microservices.bookingservice.service.BookingService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/bookings")
@Tag(name = "Booking", description = "Endpoints för att hantera tidsbokningar")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Skapa en ny bokning", description = "Registrerar en ny bokning. Kräver en giltig JWT Authorization header.")
    @ApiResponse(responseCode = "201", description = "Bokning skapad")
    @ApiResponse(responseCode = "401", description = "Obehörig (Saknad eller ogiltig JWT)")
    public ResponseEntity<BookingResponse> createBooking(@RequestBody @Valid BookingRequest request) {
        Booking toSave = request.toBookingDto();
        Booking saved = bookingService.createBooking(toSave);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BookingResponse.fromBookingResponse(saved));
    }

    @GetMapping("/{id}")
    @Cacheable(value = "bookings", key = "#id")
    @Operation(summary = "Hämta en specifik bokning", description = "Returnerar en existerande bokning via ID.")
    public ResponseEntity<BookingResponse> getBooking(@PathVariable Long id) {
        return bookingService.getBookingById(id)
                .map(BookingResponse::fromBookingResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Hämta alla bokningar", description = "Returnerar alla existerande bokningar")
    public ResponseEntity<List<BookingResponse>> getAll() {
        List<Booking> bookings = bookingService.getAllBookings();
        List<BookingResponse> responses = bookings.stream()
                .map(BookingResponse::fromBookingResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/users/{userId}")
    @Operation(summary = "Hämtar alla bokningar på en användares id", description = "Returnerar alla existerande bokningar på en användare")
    public ResponseEntity<List<BookingResponse>> getByUser(@PathVariable Long userId) {
        List<Booking> bookings = bookingService.getBookingsByUser(userId);
        List<BookingResponse> responses = bookings.stream()
                .map(BookingResponse::fromBookingResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Uppdatera status på en bokning", description = "Uppdaterar statusen på en vald bokning")
    public ResponseEntity<BookingResponse> updateStatus(@PathVariable Long id,
                                                        @RequestParam String status) {
        Booking updated = bookingService.updateStatus(id, status);
        return ResponseEntity.ok(BookingResponse.fromBookingResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Tar bort en bokning", description = "Tar bort en vald bokning från datorbasen och returnerar inget")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
}

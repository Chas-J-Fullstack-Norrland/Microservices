package org.chasapi.microservies.bookingservice.dto;

import org.chasapi.microservies.bookingservice.model.Booking;

import java.time.LocalDate;


public record BookingResponse(
        Long id,
        Long userId,
        int timeSlot,
        LocalDate bookingDate,
        String status
) {
    public static BookingResponse fromBookingResponse(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getUserId(),
                booking.getTimeSlot(),
                booking.getBookingDate(),
                booking.getStatus()
        );
    }
}

package org.chasapi.microservies.bookingservice.dto;

import jakarta.validation.constraints.FutureOrPresent;


import jakarta.validation.constraints.NotNull;
import org.chasapi.microservies.bookingservice.model.Booking;


import java.time.LocalDate;

public record BookingRequest(
        @NotNull(message = "userId is required")
        Long userId,


        int timeSlot,

        @NotNull(message = "bookingDate is required")
        @FutureOrPresent(message = "bookingDate cannot be in the past")
        LocalDate bookingDate
) {

    public Booking toBookingDto() {
        return new Booking(
                null,
                this.userId,
                this.timeSlot,
                this.bookingDate,
                null
        );

    }
}

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

    /**     * Convert this request DTO into a Booking entity.     * id and status are left null (service should set default status).     */
    public Booking toBookingDto() {
        return new Booking(
                null,               // id (new)
                this.userId,
                this.timeSlot,
                this.bookingDate,
                null                // status left null so service sets default
        );

    }
}

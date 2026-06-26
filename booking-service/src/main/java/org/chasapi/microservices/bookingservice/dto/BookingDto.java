package org.chasapi.microservices.bookingservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class BookingDto {

    @NotNull
    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    @Min(0)
    private int timeSlot;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate bookingDate;

    @NotNull
    private String status;


}

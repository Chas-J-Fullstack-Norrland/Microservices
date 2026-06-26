package org.chasapi.microservies.bookingservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class BookingDto {

    private Long id;

    @NotNull
    private Long userId;

    @Min(0)
    private int timeSlot;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate bookingDate;

    private String status;


}

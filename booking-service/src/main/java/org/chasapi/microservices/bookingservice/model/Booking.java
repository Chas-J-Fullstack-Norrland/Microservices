package org.chasapi.microservices.bookingservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private int timeSlot;

    @Column(nullable = false)
    private LocalDate bookingDate;

    @Column(nullable = false)
    private String status;

    public Booking(Long id, Long userId, int timeSlot, LocalDate bookingDate, String status) {
        this.id = id;
        this.userId = userId;
        this.timeSlot = timeSlot;
        this.bookingDate = bookingDate;
        this.status = status;
    }

    public Booking(){}

    // Getters och setters för de som inte använder lambok
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(int timeSlot) {
        this.timeSlot = timeSlot;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

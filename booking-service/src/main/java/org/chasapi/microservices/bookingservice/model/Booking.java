package org.chasapi.microservices.bookingservice.model;
import jakarta.persistence.*;
import java.time.LocalDate;
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private int timeslot;
    private Long userId;
    public Booking() {}
    public Booking(Long id, LocalDate date, int timeslot, Long userId) {
        this.id = id;
        this.date = date;
        this.timeslot = timeslot;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(int timeslot) {
        this.timeslot = timeslot;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

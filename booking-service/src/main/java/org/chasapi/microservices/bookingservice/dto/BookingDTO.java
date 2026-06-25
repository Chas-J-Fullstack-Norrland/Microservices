package org.chasapi.microservices.bookingservice.dto;
import java.time.LocalDate;
public class BookingDTO {
    private Long id;
    private LocalDate date;
    private int timeslot;
    private Long userId;
    public BookingDTO() {}
    public BookingDTO(Long id, LocalDate date, int timeslot, Long userId) {
        this.id = id;
        this.date = date;
        this.timeslot = timeslot;
        this.userId = userId;
    }
    public Long getId() {
        return id;
    }
    public LocalDate getDate() {
        return date;
    }

    public int getTimeslot() {
        return timeslot;
    }

    public Long getUserId() {
        return userId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTimeslot(int timeslot) {
        this.timeslot = timeslot;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}

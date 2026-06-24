package org.chasapi.microservies.bookingservice.dto;
import java.util.Date;
public class BookingDTO {
    private Long id;
    private Date date;
    private int timeslot;
    private long userId;

    public BookingDTO() {}
    public BookingDTO(Long id, Date date, int timeslot, Long userId) {
        this.id = id;
        this.date = date;
        this.timeslot = timeslot;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public int getTimeslot() {
        return timeslot;
    }

    public long getUserId() {
        return userId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTimeslot(int timeslot) {
        this.timeslot = timeslot;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

}

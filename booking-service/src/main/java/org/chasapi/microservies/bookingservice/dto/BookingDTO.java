package org.chasapi.microservies.bookingservice.dto;

import java.util.Date;

public class BookingDTO {
    private Long id;
    private Date date;
    private int timeslot;
    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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

package org.chasapi.microservies.bookingservice.model;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date date;
    private int timeslot;
    private Long userId;
    public Booking() {}

    public Booking(Date date, int timeslot, Long userId) {
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

    public Long getUserId() {
        return userId;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTimeslot(int timeslot) {
        this.timeslot = timeslot;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

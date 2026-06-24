package org.chasapi.microservies.bookingservice.repository;

import org.chasapi.microservies.bookingservice.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}

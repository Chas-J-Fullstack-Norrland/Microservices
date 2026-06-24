package org.chasapi.microservies.bookingservice.repository;

import org.chasapi.microservies.bookingservice.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}

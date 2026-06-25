package org.chasapi.microservices.bookingservice.repository;
import org.chasapi.microservices.bookingservice.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
public interface BookingRepository extends JpaRepository<Booking, Long> {

}

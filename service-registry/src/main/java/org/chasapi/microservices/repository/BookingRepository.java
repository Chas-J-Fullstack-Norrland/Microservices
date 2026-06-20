package org.chasapi.microservices.repository;
import org.chasapi.microservices.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {

}
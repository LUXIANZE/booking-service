package com.luxianze.bookingservice.repository;

import com.luxianze.bookingservice.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findAllBySessionId(Pageable pageable, Long sessionId);
}

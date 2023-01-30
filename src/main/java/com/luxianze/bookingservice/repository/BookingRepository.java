package com.luxianze.bookingservice.repository;

import com.luxianze.bookingservice.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Async
    CompletableFuture<Page<Booking>> findAllBySessionId(Pageable pageable, Long sessionId);
}

package com.luxianze.bookingservice.repository;

import com.luxianze.bookingservice.entity.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Page<Session> findAllByDateTimeBetween(Pageable pageable, LocalDateTime startDate, LocalDateTime endDate);
}

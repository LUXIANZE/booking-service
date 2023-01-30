package com.luxianze.bookingservice.repository;

import com.luxianze.bookingservice.entity.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Page<Session> findAllByDateTimeBetween(Pageable pageable, LocalDateTime startDate, LocalDateTime endDate);

    @Async
    @Query("select s from Session s where s.id = :sessionId")
    CompletableFuture<Optional<Session>> asyncFindById(Long sessionId);
}

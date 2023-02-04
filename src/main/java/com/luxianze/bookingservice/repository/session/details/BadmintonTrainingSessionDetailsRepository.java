package com.luxianze.bookingservice.repository.session.details;

import com.luxianze.bookingservice.entity.session.details.BadmintonTrainingSessionDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BadmintonTrainingSessionDetailsRepository extends JpaRepository<BadmintonTrainingSessionDetails, Long> {
    Optional<BadmintonTrainingSessionDetails> findBySessionId(Long sessionId);
}

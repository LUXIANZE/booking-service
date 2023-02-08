package com.luxianze.bookingservice.service;

import com.luxianze.bookingservice.service.dto.BadmintonTrainingSessionDetailsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BadmintonTrainingSessionDetailsService {
    Page<BadmintonTrainingSessionDetailsDTO> findAll(Pageable pageable);
    Optional<BadmintonTrainingSessionDetailsDTO> findBySessionId(Long sessionId);
    Optional<BadmintonTrainingSessionDetailsDTO> findById(Long id);
    BadmintonTrainingSessionDetailsDTO create(BadmintonTrainingSessionDetailsDTO badmintonTrainingSessionDetailsDTO) throws Exception;
    BadmintonTrainingSessionDetailsDTO update(BadmintonTrainingSessionDetailsDTO badmintonTrainingSessionDetailsDTO) throws Exception;
}

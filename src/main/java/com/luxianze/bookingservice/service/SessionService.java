package com.luxianze.bookingservice.service;

import com.luxianze.bookingservice.service.dto.SessionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

public interface SessionService {
    Page<SessionDTO> getAll(Pageable pageable, LocalDate date);
    Optional<SessionDTO> getById(Long id);
    SessionDTO create(SessionDTO sessionDTO);
    SessionDTO update(SessionDTO sessionDTO);
}

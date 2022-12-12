package com.luxianze.bookingservice.service;

import com.luxianze.bookingservice.service.dto.SessionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SessionService {
    Page<SessionDTO> getAll(Pageable pageable);
    Optional<SessionDTO> getById(Long id);
    SessionDTO create(SessionDTO sessionDTO);
    SessionDTO update(SessionDTO sessionDTO);
}

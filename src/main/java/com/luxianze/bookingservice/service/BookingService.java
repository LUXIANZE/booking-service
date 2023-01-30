package com.luxianze.bookingservice.service;

import com.luxianze.bookingservice.service.dto.BookingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface BookingService {
    BookingDTO create(BookingDTO bookingDTO) throws Exception;
    BookingDTO update(BookingDTO bookingDTO);
    Page<BookingDTO> getAll(Pageable pageable, Long sessionId) throws ExecutionException, InterruptedException;
    Optional<BookingDTO> getById(Long id);
}

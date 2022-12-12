package com.luxianze.bookingservice.service;

import com.luxianze.bookingservice.service.dto.BookingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BookingService {
    BookingDTO create(BookingDTO bookingDTO);
    BookingDTO update(BookingDTO bookingDTO);
    Page<BookingDTO> getAll(Pageable pageable);
    Optional<BookingDTO> getById(Long id);
}

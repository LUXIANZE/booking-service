package com.luxianze.bookingservice.controller.booking;

import com.luxianze.bookingservice.service.BookingService;
import com.luxianze.bookingservice.service.dto.BookingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("booking")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity<Page<BookingDTO>> getBookings(Pageable pageable, @RequestParam Long sessionId) {
        return ResponseEntity
                .ok(bookingService.getAll(pageable, sessionId));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Long bookingId) {
        return bookingService.getById(bookingId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BookingDTO> createBooking(@RequestBody BookingDTO bookingDTO) {
        return ResponseEntity
                .ok(bookingService.create(bookingDTO));
    }

    @PutMapping
    public ResponseEntity<BookingDTO> updateBooking(@RequestBody BookingDTO bookingDTO) {
        return ResponseEntity
                .ok(bookingService.update(bookingDTO));
    }
}
